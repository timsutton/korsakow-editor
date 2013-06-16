package org.korsakow.domain.mapper.input;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;
import org.dsrg.soenea.uow.ObjectRemovedException;
import org.korsakow.domain.Project;
import org.korsakow.domain.ProjectFactory;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.proxy.ImageProxy;
import org.korsakow.domain.proxy.InterfaceListProxy;
import org.korsakow.domain.proxy.InterfaceProxy;
import org.korsakow.domain.proxy.KeywordCollectionProxy;
import org.korsakow.domain.proxy.MediaListProxy;
import org.korsakow.domain.proxy.ProjectProxy;
import org.korsakow.domain.proxy.RuleListProxy;
import org.korsakow.domain.proxy.SettingsProxy;
import org.korsakow.domain.proxy.SnuListProxy;
import org.korsakow.domain.proxy.SoundProxy;
import org.korsakow.domain.proxy.UnknownMediaProxy;
import org.korsakow.ide.DataRegistry;
import org.korsakow.services.finder.ProjectFinder;
import org.korsakow.services.util.ColorFactory;

public class ProjectInputMapper {
	
	public static Project map(long id) throws MapperException {
		try {
			return IdentityMap.get(id, Project.class);
		} catch (ObjectRemovedException e) {
			throw new DomainObjectNotFoundException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		try {
			ResultSet rs = ProjectFinder.find(id);
			if(!rs.next()) throw new DomainObjectNotFoundException("That object doesn't exist:" + id);
			Project a = getProject(rs);
			rs.close();
			return a;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	public static List<? extends IProject> findBySound(long sound_id) throws MapperException {
		List<IProject> list = new ArrayList<IProject>();
		try {
			ResultSet rs = null;
			rs = ProjectFinder.findBySoundId(sound_id);
			while (rs.next()) {
				list.add(new ProjectProxy(rs.getLong("id")));
			}
			rs.close();
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
		return list;
	}

	public static Project find() throws MapperException {
		ResultSet rs = null;
		try {
			rs = ProjectFinder.find();
			if (!rs.next())
				throw new DomainObjectNotFoundException("");
			if (rs.next())
				throw new MapperException("Multiple projects found!");
			Project project = getProject(rs);
			rs.close();
			return project;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	
	private static Project getProject(ResultSet rs) throws SQLException, MapperException {
		long id = rs.getObject("id")!=null?rs.getLong("id"):DataRegistry.getMaxId(); // importing default project
		
		try {
			return IdentityMap.get(id, Project.class);
		} catch (ObjectRemovedException e) {
			throw new MapperException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		
		Project Project = ProjectFactory.createClean(
				id, 
				rs.getLong("version"),
				rs.getString("name"),
				new KeywordCollectionProxy(id),
				rs.getObject("movieWidth")!=null?rs.getInt("movieWidth"):null,
				rs.getObject("movieHeight")!=null?rs.getInt("movieHeight"):null,
				rs.getObject("backgroundSoundId")!=null?new SoundProxy(rs.getLong("backgroundSoundId")):null,
				rs.getFloat("backgroundSoundVolume"),
				rs.getBoolean("backgroundSoundLooping"),
				rs.getObject("clickSoundId")!=null?new SoundProxy(rs.getLong("clickSoundId")):null,
				rs.getFloat("clickSoundVolume"),
				rs.getObject("backgroundImageId")!=null?new ImageProxy(rs.getLong("backgroundImageId")):null,
				rs.getObject("backgroundColor")!=null?ColorFactory.createRGB(rs.getString("backgroundColor")):null,
				rs.getObject("splashScreenMediaId")!=null?new UnknownMediaProxy(rs.getLong("splashScreenMediaId")):null,
				rs.getBoolean("randomLinkMode"),
				rs.getBoolean("keepLinksOnEmptySearch"),
				rs.getObject("maxLinks")!=null?rs.getLong("maxLinks"):null,
				rs.getObject("defaultInterface")!=null?new InterfaceProxy(rs.getLong("defaultInterface")):null,
				new RuleListProxy(id),
				new SnuListProxy(id),
				new InterfaceListProxy(id),
				new MediaListProxy(id),
				rs.getObject("settingsId")!=null?new SettingsProxy(rs.getLong("settingsId")):null,
				rs.getString("uuid")
				);
		return Project;
	}
}
