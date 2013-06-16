package org.korsakow.domain;

import java.util.HashMap;
import java.util.Map;

import org.dsrg.soenea.domain.DomainObject;
import org.dsrg.soenea.domain.interf.IDomainObject;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;
import org.korsakow.domain.interf.IEvent;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IPredicate;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IText;
import org.korsakow.domain.interf.ITrigger;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.domain.proxy.KDomainObjectProxy;
import org.korsakow.domain.proxy.UnknownMediaProxy;

public class CloneFactory {
	private static interface ICloneFactory<T extends IDomainObject<Long>>
	{
		T clone(IDomainObject<Long> src);
	}
	private static Map<Class<? extends DomainObject<Long>>, ICloneFactory<? extends DomainObject<Long>>> registry = new HashMap<Class<? extends DomainObject<Long>>, ICloneFactory<? extends DomainObject<Long>>>();
	static {
		registry.put(Image.class, new ICloneFactory<Image>() {
			public Image clone(IDomainObject<Long> src) {
				return ImageFactory.copy((IImage)src);
			}
		});
		registry.put(Video.class, new ICloneFactory<Video>() {
			public Video clone(IDomainObject<Long> src) {
				return VideoFactory.copy((IVideo)src);
			}
		});
		registry.put(Text.class, new ICloneFactory<Text>() {
			public Text clone(IDomainObject<Long> src) {
				return TextFactory.copy((IText)src);
			}
		});
		registry.put(Sound.class, new ICloneFactory<Sound>() {
			public Sound clone(IDomainObject<Long> src) {
				return SoundFactory.copy((ISound)src);
			}
		});
		registry.put(Interface.class, new ICloneFactory<Interface>() {
			public Interface clone(IDomainObject<Long> src) {
				return InterfaceFactory.copy((IInterface)src);
			}
		});
		registry.put(Event.class, new ICloneFactory<Event>() {
			public Event clone(IDomainObject<Long> src) {
				return EventFactory.copy((IEvent)src);
			}
		});
		registry.put(Rule.class, new ICloneFactory<Rule>() {
			public Rule clone(IDomainObject<Long> src) {
				return RuleFactory.copy((IRule)src);
			}
		});
		registry.put(Predicate.class, new ICloneFactory<Predicate>() {
			public Predicate clone(IDomainObject<Long> src) {
				return PredicateFactory.copy((IPredicate)src);
			}
		});
		registry.put(Trigger.class, new ICloneFactory<Trigger>() {
			public Trigger clone(IDomainObject<Long> src) {
				return TriggerFactory.copy((ITrigger)src);
			}
		});
		registry.put(Widget.class, new ICloneFactory<Widget>() {
			public Widget clone(IDomainObject<Long> src) {
				return WidgetFactory.copy((IWidget)src);
			}
		});
		registry.put(Snu.class, new ICloneFactory<Snu>() {
			public Snu clone(IDomainObject<Long> src) {
				return SnuFactory.copy((ISnu)src);
			}
		});
	}
	
	public static <T extends IDomainObject<Long>> T clone(T src)
	{
		Class<? extends IDomainObject<Long>> clazz;
		if (src instanceof DomainObjectProxy<?, ?>)
			clazz = ((KDomainObjectProxy)src).getInnerClass();
		else
			clazz = (Class<? extends IDomainObject<Long>>) src.getClass();
		if (src instanceof UnknownMediaProxy)
			src = (T) ((UnknownMediaProxy)src).getMedia();
		
		if (!registry.containsKey(clazz))
			throw new RuntimeDomainException("missing CloneFactory for: " + clazz);
		
		return (T) registry.get(clazz).clone(src);
	}
}
