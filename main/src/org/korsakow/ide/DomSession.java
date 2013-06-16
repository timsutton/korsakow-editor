/**
 * 
 */
package org.korsakow.ide;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.w3c.dom.Document;

/**
 * Implements simple thread-safe read/write on a dom via commits. Any thread may call getDocument() to obtain a thread-local
 * copy of the DOM. Changes may then be committed via commit().
 * 
 * Evidently, complex writes are not supported. Trying to commit an out-of-date document will throw a ConcurrentModificationException.
 * 
 * The intended use case is either a single thread writing with multiple threads reading, or at least a situation where the writes
 * are otherwise regulated by the application such that invalid commits won't occur.
 * 
 * TODO: not so happy with the class name
 * 
 * @author d
 *
 */
public class DomSession
{
	public static class VersionInfo
	{
		private int version = 0;
		private Document document = null;
		
		public VersionInfo()
		{
		}
		public VersionInfo(VersionInfo other)
		{
			setVersion(other.getVersion());
			if (other.getDocument() != null)
				this.setDocument(cloneDocument(other.getDocument()));
		}
		
		public int getVersion()
		{
			return version;
		}
		public void setVersion(int version)
		{
			this.version = version;
		}
		public Document getDocument()
		{
			return document;
		}
		public void setDocument(Document document)
		{
			this.document = document;
		}
	}
	private static Document cloneDocument(Document document)
	{
		return (Document)document.cloneNode(true);
	}
	
	/**
	 * The working copy.
	 */
	private final ThreadLocal<VersionInfo> threadLocal = new ThreadLocal<VersionInfo>() {
		@Override
		protected VersionInfo initialValue()
		{
			VersionInfo info = new VersionInfo();
			synchronized (headLock) {
				info.setVersion(headVersion.getVersion());
			}
			return info;
		}
	};
	private final VersionInfo headVersion = new VersionInfo();
	private final Object headLock = new Object();
	
	private final List<VersionInfo> history = new ArrayList<VersionInfo>();
	
	public DomSession(Document document)
	{
		headVersion.setVersion(0);
		headVersion.setDocument(document);
	}
	
	public VersionInfo getThreadLocal()
	{
		return threadLocal.get();
	}
	
	/**
	 * This method was introduced for unit testing.
	 * @return
	 */
	public Document getHeadDocument()
	{
		synchronized (headLock) {
			return cloneDocument(headVersion.getDocument());
		}
	}
	/**
	 * This method was introduced for unit testing.
	 * @return
	 */
	public long getHeadVersion()
	{
		synchronized (headLock) {
			return headVersion.getVersion();
		}
	}
	public long getVersion()
	{
		VersionInfo localInfo = getThreadLocal();
		return localInfo.getVersion();
	}
	public boolean isUptoDate()
	{
		VersionInfo localInfo = getThreadLocal();
		synchronized (headLock) {
			return localInfo.getVersion() == headVersion.getVersion();
		}
	}
	/**
	 * Thread-safe call which obtains a thread-local copy of the DOM. The local.getVersion() is checked against the head.getVersion()
	 * so that a copy need not be made every call but only when the head has changed.
	 * 
	 * That is, the same Document instance is returned unless the head has changed.
	 * 
	 * @return
	 */
	public Document getDocument()
	{
		VersionInfo localInfo = getThreadLocal();
		synchronized (headLock) {
			if (localInfo.getDocument() == null || localInfo.getVersion() != headVersion.getVersion()) {
				localInfo.setDocument(cloneDocument(headVersion.getDocument()));
				localInfo.setVersion(headVersion.getVersion());
			}
		}
		return localInfo.getDocument();
	}
	/**
	 * This is effectively like doing local dom manipulations.
	 * @param doc
	 */
	public void setDocument(Document doc)
	{
		VersionInfo localInfo = getThreadLocal();
		localInfo.setDocument(doc);
	}
	/**
	 * Commits the local document to the head. If the local.getVersion() does not match the head.getVersion() (ie another thread has committed since the last get),
	 * a ConcurrentModificationException is thrown.
	 * 
	 *.getVersion() is incremented even if the current document is identical to the head document
	 * 
	 * @throws ConcurrentModificationException
	 */
	public void commit() throws ConcurrentModificationException {
		VersionInfo localInfo = getThreadLocal();

		synchronized (headLock) {
			if (localInfo.getVersion() != headVersion.getVersion())
				throw new ConcurrentModificationException(String.format("another thread has committed v%d since the last update v%d", headVersion.getVersion(), localInfo.getVersion()));

			VersionInfo historyInfo = new VersionInfo();
			historyInfo.setVersion(headVersion.getVersion());
			historyInfo.setDocument(cloneDocument(headVersion.getDocument()));
			history.add(historyInfo);
			while (history.size() > 1)
				history.remove(0);
			
			if (localInfo.getDocument() != null) {
				headVersion.setDocument(cloneDocument(localInfo.getDocument()));
			}
			headVersion.setVersion(headVersion.getVersion() + 1);
			localInfo.setVersion(headVersion.getVersion());

		}
		localInfo = null;
	}
	/**
	 * Reverts the local docment to the head revision, discarding any local changes.
	 */
	public void rollbackToHead() {
		VersionInfo localInfo = getThreadLocal();
		synchronized (headLock) {
			localInfo.setDocument(null);
			localInfo.setVersion(headVersion.getVersion());
		}
	}
	/**
	 * 
	 * @return false if the rollback would cause a loss of information
	 */
	public boolean tryRollbackToHead() {
		VersionInfo localInfo = getThreadLocal();
		synchronized (headLock) {
			if (localInfo.getVersion() == headVersion.getVersion()) {
				localInfo.setDocument(null);
				localInfo.setVersion(headVersion.getVersion());
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Rolls back the head to the previous.getVersion() in the history.
	 * If history is empty, equivalent to rollback()
	 */
	public void rollbackHeadToPreviousVersion()
	{
		VersionInfo localInfo = getThreadLocal();
		synchronized (headLock)
		{
			VersionInfo prevVersion = null;
			
			if (!history.isEmpty())
				prevVersion = history.remove(history.size()-1);
			else
				prevVersion = headVersion;
			
			localInfo.setVersion(prevVersion.getVersion());
			headVersion.setVersion(localInfo.getVersion());
			headVersion.setDocument(cloneDocument(prevVersion.getDocument()));
			localInfo.setDocument(cloneDocument(prevVersion.getDocument()));
		}
	}
	
	public List<VersionInfo> getHistory()
	{
		List<VersionInfo> copy = new ArrayList<VersionInfo>();
		synchronized (headLock)
		{
			for (VersionInfo info : history)
				copy.add(new VersionInfo(info));
		}
		return copy;
	}
}
