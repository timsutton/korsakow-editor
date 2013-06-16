package org.korsakow.ide.ui.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;
import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.ISnu.BackgroundSoundMode;
import org.korsakow.domain.mapper.input.ImageInputMapper;
import org.korsakow.domain.mapper.input.MediaInputMapper;
import org.korsakow.domain.mapper.input.SoundInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.code.RuleParserException;
import org.korsakow.ide.code.k5.K5Code;
import org.korsakow.ide.code.k5.K5CodeGenerator2;
import org.korsakow.ide.code.k5.K5RuleParser2;
import org.korsakow.ide.code.k5.K5Symbol;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.resources.media.MediaFactory;
import org.korsakow.ide.resources.media.Playable;
import org.korsakow.ide.rules.RuleType;
import org.korsakow.ide.ui.ResourceEditor;
import org.korsakow.ide.ui.components.code.CodeTable;
import org.korsakow.ide.ui.components.code.CodeTableModel;
import org.korsakow.ide.ui.model.RuleModel;
import org.korsakow.ide.ui.resources.SnuResourceView;
import org.korsakow.ide.util.UIUtil;


public class SnuResourceEditorController
{
	private final class MyWindowFocusListener implements WindowFocusListener
	{
		private final CodeTable codeTable;

		private MyWindowFocusListener(CodeTable codeTable)
		{
			this.codeTable = codeTable;
		}

		@Override
		public void windowLostFocus(WindowEvent e)
		{
			if (codeTable.getCellEditor() != null)
				codeTable.getCellEditor().stopCellEditing();
		}

		@Override
		public void windowGainedFocus(WindowEvent e)
		{
		}
	}
	private class ValidateDocumentListener implements DocumentListener
	{
		public void changedUpdate(DocumentEvent e) {
			validate();
		}
		public void insertUpdate(DocumentEvent e) {
			validate();
		}
		public void removeUpdate(DocumentEvent e) {
			validate();
		}
	}
	private class ValidateActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			validate();
		}
	}
	private class UpdateCodeTimeFromMainMediaListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent e) {
			Playable media = resourceView.getMainMediaPanel().getPlayable();
			if (media == null)
				return;
			if (media.isPlaying())
				return;

			final CodeTable codeTable = resourceView.getCodeTable();
			int viewRowIndex = codeTable.getSelectedRow();
			if (viewRowIndex != -1) {
				int viewColIndex = codeTable.getSelectedColumn();
				if (viewColIndex == -1 || viewColIndex != codeTable.getColumnIndex(CodeTable.TIME_IDENTIFIER))
					viewRowIndex = -1;
				
				if (viewRowIndex != -1 && null == codeTable.getValueAt(viewRowIndex, codeTable.getColumnIndex(CodeTable.TIME_IDENTIFIER)))
					return;
			}
			
			final int modelRowIndex;
			if (viewRowIndex == -1)
				return;
//				modelRowIndex = codeTable.getModel().getEmptyRow();
//			else
				modelRowIndex = codeTable.convertRowIndexToModel(viewRowIndex);
			long time = media.getTime();
			resourceView.getCodeTable().getModel().setTimeAt(time, modelRowIndex);
		}
	}
	private class UpdateVideoFromCodeTableListener implements ListSelectionListener, TableModelListener
	{
		private void update()
		{
			// sadly you would think the selected row could not be other than -1 if
			// the row count was 0, but empirical analysis proved otherwise
			final CodeTable codeTable = resourceView.getCodeTable();
			if (codeTable.getRowCount() == 0)
				return;
			int viewColIndex = codeTable.getSelectedColumn();
			if (viewColIndex == -1 || viewColIndex != codeTable.getColumnIndex(CodeTable.TIME_IDENTIFIER))
				return;
			int row = codeTable.getSelectedRow();
			if (row == -1)
				return;
			// this was a bug that should not have been
			if (row >= codeTable.getRowCount())
				return;
			Long time = codeTable.getModel().getTimeAt(codeTable.convertRowIndexToModel(row));
			if (time == null)
				return;
			Playable media = resourceView.getMainMediaPanel().getPlayable();
			if (media == null)
				return;
			if (!media.isPlaying()) {
				media.setTime(time);
			}
		}

		public void valueChanged(ListSelectionEvent e) {
			update();
		}
		public void tableChanged(TableModelEvent e) {
			final CodeTable codeTable = resourceView.getCodeTable();
			if (e.getType()==TableModelEvent.UPDATE && e.getColumn() == codeTable.getColumnIndex(CodeTable.TIME_IDENTIFIER))
				update();
		}
	}
	private class CodeTableCanonicalFormListener implements TableModelListener
	{
		CodeTable codeTable;
		TableColumnModel columnModel;
		public CodeTableCanonicalFormListener(CodeTable codeTable, TableColumnModel columnModel)
		{
			this.codeTable = codeTable;
			this.columnModel = columnModel;
		}
		public void tableChanged(TableModelEvent e) {
			if (e.getType()==TableModelEvent.DELETE)
				return;
			
			int column = e.getColumn();
			if (column == -1)
				return;
			
			if (codeTable.getColumnIndex(CodeTable.CODE_IDENTIFIER) == e.getColumn()) {
				// we wish to maintain the K3 rules in canonical form
				restoreCodeCanonicalForm();
				
				// if you add code to a line with no time, it gets the timecode of the row before.
				// its coded for the general case, but its intended in particular for editing the last row
				CodeTableModel model = codeTable.getModel();
				int row = e.getFirstRow();
				if (model.getTimeAt(row) == null &&
					model.getCodeAt(row).getRawCode().trim().length() > 0)
				{
					Long time = null;
					for (int i = row - 1; i > -1; --i) {
						if (model.getTimeAt(i) != null) {
							time = model.getTimeAt(i);
							break;
						}
					}
					if (time == null)
						time = 0L;
					model.setTimeAt(time, row);
				}
			}
			UIUtil.runUITaskLater(new Runnable() {
				public void run() {
					// remove empty rows
					for (int i = 0; i < codeTable.getModel().getRowCount()-1; ++i) {
						if (codeTable.getModel().getTimeAt(i) == null &&
							(codeTable.getModel().getCodeAt(i) == null || codeTable.getModel().getCodeAt(i).getRawCode().trim().length()==0))
						{
							codeTable.getModel().removeRow(i);
							--i; // compensate for removal
						}
					}
				}
			});
			// bound to length of media
			if (codeTable.getColumnIndex(CodeTable.TIME_IDENTIFIER) == e.getColumn()) {
				final Playable media = resourceView.getMainMediaPanel().getPlayable();
				if (media != null) {
					for (int i = e.getFirstRow(); i <= e.getLastRow() && i < codeTable.getRowCount(); ++i)
					{
						Long time = codeTable.getModel().getTimeAt(i);
						if (time != null && time > media.getDuration())
						{
							final int I = i;
							codeTable.getModel().setTimeAt(media.getDuration(), I);
						}
					}
				}
			}
		}
	}
	/**
	 * Reattaches listenrs to the model when it changes
	 * @author d
	 *
	 */
	private class CodeTableModelChangeListener implements PropertyChangeListener
	{
		public void propertyChange(PropertyChangeEvent event) {
			TableModel newModel = (TableModel)event.getNewValue();
			newModel.addTableModelListener(codeTableModelListener);
		}
	}
	private class BackgroundSoundChangeListener implements ActionListener
	{
		public void actionPerformed( ActionEvent event ) {
			Long mediaId = resourceView.getMainMediaId();
			if ( mediaId == null )
				return;
			IMedia media;
			try {
				media = MediaInputMapper.map( mediaId );
			} catch (MapperException e) {
				Logger.getLogger(getClass()).error("", e);
				return;
			}
			// this instanceof check is actually part of the business logic
			// we only adjust the media length if it is an image
			if ( !ResourceType.IMAGE.isInstance(media) )
				return;
			IImage image;
			try {
				image = ImageInputMapper.map(media.getId());
			} catch (MapperException e) {
				Logger.getLogger(getClass()).error("", e);
				return;
			}
			
			// this null check is actually part of the business logic
			// we only adjust the media length if it was not already manually set
			if ( image.getDuration() != null )
				return;
			if (resourceView.getBackgroundSoundMode() != BackgroundSoundMode.SET)
				return;
			Long bgId = resourceView.getBackgroundSoundId();
			ISound bg;
			try {
				bg = SoundInputMapper.map( bgId );
			} catch (MapperException e) {
				Logger.getLogger(getClass()).error("", e);
				return;
			}
			if ( bg == null )
				return;
			final long duration = MediaFactory.getMediaNoThrow( bg ).getDuration();
			resourceView.setMainMediaCustomDuration( duration );
			
			Application.getInstance().showOneTimeAlertDialog( "AdjustSnuDurationFromBackgroundSound", (Component)event.getSource(), "", "The SNU's duration has been adjusted to match the selected background sound.\nThis happened because the SNU's media is an image and you have not previously adjusted its duration.");
		}
	}
	private final ResourceEditor editor;
	private final SnuResourceView resourceView;
	private CodeTableCanonicalFormListener codeTableModelListener;
	/**
	 * @param resourceId null for inserts, non-null for updates
	 */
	public SnuResourceEditorController(ResourceEditor editor, Long resourceId)
	{
		this.editor = editor;
		resourceView = (SnuResourceView)editor.getResourceView();
		final CodeTable codeTable = resourceView.getCodeTable();
		codeTable.addPropertyChangeListener("model", new CodeTableModelChangeListener());
		codeTable.getModel().addTableModelListener(codeTableModelListener = new CodeTableCanonicalFormListener(codeTable, codeTable.getColumnModel()));
		UpdateVideoFromCodeTableListener updateVideoFromCodeTableListener = new UpdateVideoFromCodeTableListener();
		codeTable.getSelectionModel().addListSelectionListener(updateVideoFromCodeTableListener);
		codeTable.getModel().addTableModelListener(updateVideoFromCodeTableListener);
		editor.addWindowFocusListener(new MyWindowFocusListener(codeTable));
		resourceView.getMainMediaPanel().addSeekSliderChangeListener(new UpdateCodeTimeFromMainMediaListener());
		resourceView.getKeywordArea().getDocument().addDocumentListener(new ValidateDocumentListener());
		resourceView.getStarerCheck().addActionListener(new ValidateActionListener());
		resourceView.getEnderCheck().addActionListener(new ValidateActionListener());
//		resourceView.addEventActionListener(new ShowEventEditorAction(editor, resourceView));
		resourceView.addBackgroundSoundChangeListener( new BackgroundSoundChangeListener() );
		validate();
	}
	public boolean restoreCodeCanonicalForm()
	{
		K5RuleParser2 parser = new K5RuleParser2();
		K5CodeGenerator2 generator = new K5CodeGenerator2();
		CodeTableModel codeModel = resourceView.getCodeTable().getModel();
		boolean valid = true;
		for (int i = 0; i < codeModel.getRowCount(); ++i) {
			K5Code code = codeModel.getCodeAt(i);
			try {
				String rawCode = code.getRawCode();
				if (i == 0)
					rawCode = generator.createClearPreviousLinks() + K5Symbol.DEFAULT_STATEMENT_SEPARATOR_STRING + " " + rawCode;
				else
					rawCode = generator.createKeepPreviousLinks() + K5Symbol.DEFAULT_STATEMENT_SEPARATOR_STRING + " " + rawCode;
				
				List<RuleModel> rules = parser.parse(rawCode);
				
				boolean haveClearScores = false;
				List<RuleModel> toRemove = new ArrayList<RuleModel>();
				for (RuleModel rule : rules)
					if (rule.getType() == RuleType.ClearScores) {
						haveClearScores = true;
						toRemove.add(rule);
					}
				if (i == 0)
					rules.removeAll(toRemove);
				
				K5Code canonicalCode = generator.createK5CodeOmitUnsupported(rules, i==0);
				String canonicalRaw = canonicalCode.getRawCode();
				if (i==0) {
					if (!haveClearScores)
						canonicalRaw = generator.createKeepPreviousLinks() + K5Symbol.DEFAULT_STATEMENT_SEPARATOR_STRING + " " + canonicalRaw;
				}
				code.setRawCode(canonicalRaw);
				code.setValid(true);
			} catch (RuleParserException e) {
				e.printStackTrace();
				valid = false;
				code.setValid(false);
				break;
			}
		}
		resourceView.getCodeTable().repaint();
		return valid;
	}
	public void validate()
	{
		try {
			validateMainMedia();
			
			validateKeywords();

			editor.getOKButton().setEnabled(true);
			if (_validate_wasPressed) {
				// this is a hack. on focus lost we set to disabled which cancels the button press
				// when clicking the ok button
				editor.getOKButton().getModel().setRollover(true);
				editor.getOKButton().getModel().setArmed(true);
				editor.getOKButton().getModel().setPressed(true);
			}
			_validate_wasPressed = false;
			resourceView.setStatusText("");
			
			if (!restoreCodeCanonicalForm())
				throw new ValidationException("Invalid rule");
		} catch (ValidationException e) {
			_validate_wasPressed = editor.getOKButton().getModel().isPressed();
			editor.getOKButton().setEnabled(false);
			resourceView.setStatusText(e.getMessage());
		}
	}
	private boolean _validate_wasPressed = false;
	public void validateKeywords() throws ValidationException
	{
		if (resourceView.getStarter() || resourceView.getEnder())
			return;
		Collection<String> tokens = resourceView.getKeywordArea().getTokens();
		if (tokens.isEmpty()) {
			_validate_wasPressed = editor.getOKButton().getModel().isPressed();
			throw new ValidationException("SNU must have keywords unless its a startfilm or endfilm");
		}
	}
	private void validateMainMedia() throws ValidationException
	{
		if (resourceView.getMainMediaId() == null)
			throw new ValidationException("SNU must have a media");
	}
	private static class ValidationException extends Exception
	{
		public ValidationException(String message)
		{
			super(message);
		}
	}
}
