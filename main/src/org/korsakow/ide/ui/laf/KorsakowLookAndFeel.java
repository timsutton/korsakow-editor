package org.korsakow.ide.ui.laf;

import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.UIDefaults;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicLookAndFeel;

import org.apache.log4j.Logger;
import org.korsakow.ide.Application;
import org.korsakow.ide.util.Platform;
import org.korsakow.ide.util.ResourceManager;
import org.korsakow.ide.util.UIResourceManager;

import com.sun.java.swing.plaf.motif.MotifFileChooserUI;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import com.sun.java.swing.plaf.motif.MotifTreeCellRenderer;

public class KorsakowLookAndFeel extends BasicLookAndFeel
{
    @Override
	public UIDefaults getDefaults()
    {
    	UIDefaults defaults = new UIDefaults();

    	initClassDefaults(defaults);
    	
    	initSystemColorDefaults(defaults);
		super.initComponentDefaults(defaults);
		KorsakowDefaultKeybindings.installKeybindings(defaults);
		LafProperties lafProperties = null;
    	try {
			InputStream inputStream;
			inputStream = ResourceManager.getResourceStream("laf.xml");
			lafProperties = new LafProperties(inputStream);
			lafProperties.installColors(defaults);
		} catch (Exception e) {
			Logger.getLogger(Application.class).error("", e);
		}
		try {
			if (lafProperties != null)
				lafProperties.installComponents(defaults, lafProperties.getDefaultValues());
		} catch (Exception e) {
			Logger.getLogger(Application.class).error("", e);
		}
		initComponentDefaults(defaults);
    	return defaults;
    }
//		Enumeration e = faults.keys();
//		TreeSet set = new TreeSet();
//		while (e.hasMoreElements()) {
//			Object item = e.nextElement();
//			if (!item.toString().endsWith("UI"))
//				set.add(e.nextElement());
//		}
//		for (Object item : set)
//			System.out.println(item +" " + faults.get(item));
//    	return faults;
//    }
    @Override
	protected void initClassDefaults(UIDefaults table)
    {
    	table.put("Panel.opaque", false);
//		UIDefaults def = UIManager.getLookAndFeelDefaults();
//		Enumeration e = def.keys();
//		while (e.hasMoreElements()) {
//			Object item = e.nextElement();
//			if (item.toString().endsWith("UI"))
//				System.out.println(item +" " + def.get(item));
//		}
		
        super.initClassDefaults(table);
        String korsakowPackageName = "org.korsakow.ide.ui.laf.";
        String motifPackageName = "com.sun.java.swing.plaf.motif.";
        String basicPackageName = "javax.swing.plaf.basic.";

        Object[] uiDefaults = {
                   "ButtonUI", korsakowPackageName + "KorsakowButtonUI",
                 "CheckBoxUI", korsakowPackageName + "KorsakowCheckBoxUI",
//            "DirectoryPaneUI", motifPackageName + "MotifDirectoryPaneUI",
              "FileChooserUI", MotifFileChooserUI.class.getCanonicalName(),
                    "LabelUI", korsakowPackageName + "KorsakowLabelUI",
//                  "MenuBarUI", korsakowPackageName + "MotifMenuBarUI",
//                     "MenuUI", korsakowPackageName + "MotifMenuUI",
                 "MenuItemUI", korsakowPackageName + "KorsakowMenuItemUI",
//         "CheckBoxMenuItemUI", korsakowPackageName + "MotifCheckBoxMenuItemUI",
//      "RadioButtonMenuItemUI", korsakowPackageName + "MotifRadioButtonMenuItemUI",
              "RadioButtonUI", korsakowPackageName + "KorsakowRadioButtonUI",
              "ToggleButtonUI", korsakowPackageName + "KorsakowToggleButtonUI",
//             "ToggleButtonUI", korsakowPackageName + "MotifToggleButtonUI",
//                "PopupMenuUI", korsakowPackageName + "MotifPopupMenuUI",
//              "ProgressBarUI", korsakowPackageName + "MotifProgressBarUI",
//                "ScrollBarUI", korsakowPackageName + "MotifScrollBarUI",
//            "ScrollPaneUI", korsakowPackageName + "MotifScrollPaneUI",
                   "SliderUI", korsakowPackageName + "KorsakowSliderUI",
//                "SplitPaneUI", korsakowPackageName + "MotifSplitPaneUI",
               "TabbedPaneUI", korsakowPackageName + "KorsakowTabbedPaneUI",
                   "TreeUI", korsakowPackageName + "KorsakowTreeUI",
//                 "TextAreaUI", korsakowPackageName + "MotifTextAreaUI",
                 "TableHeaderUI", korsakowPackageName + "KorsakowTableHeaderUI",
                 "TableUI", korsakowPackageName + "KorsakowTableUI",
                 "ListUI", korsakowPackageName + "KorsakowListUI",
//               "PanelUI", korsakowPackageName + "KorsakowPanelUI",
               "CollapsiblePaneHeaderUI", korsakowPackageName + "KorsakowCollapsiblePaneHeaderUI",
                "TextFieldUI", korsakowPackageName + "KorsakowTextFieldUI",
//            "PasswordFieldUI", korsakowPackageName + "MotifPasswordFieldUI",
//                 "TextPaneUI", korsakowPackageName + "MotifTextPaneUI",
//               "EditorPaneUI", korsakowPackageName + "MotifEditorPaneUI",
//                     "TreeUI", korsakowPackageName + "MotifTreeUI",
//            "InternalFrameUI", korsakowPackageName + "MotifInternalFrameUI",
//              "DesktopPaneUI", korsakowPackageName + "MotifDesktopPaneUI",
//                "SeparatorUI", korsakowPackageName + "MotifSeparatorUI",
//       "PopupMenuSeparatorUI", korsakowPackageName + "MotifPopupMenuSeparatorUI",
//               "OptionPaneUI", korsakowPackageName + "MotifOptionPaneUI",
                 "ComboBoxUI", korsakowPackageName + "KorsakowComboBoxUI",
//              "DesktopIconUI", korsakowPackageName + "MotifDesktopIconUI"
                 "RootPaneUI", korsakowPackageName + "KorsakowRootPaneUI",
        };

        table.putDefaults(uiDefaults);
    }

    /**
     */
    @Override
	protected void initSystemColorDefaults(UIDefaults table)
    {
    	super.initSystemColorDefaults(table);
        String[] defaultSystemColors = {
                     "window2", "#424242", /* Default color for the interior of windows */
//            "activeCaption", "#000080", /* Color for captions (title bars) when they are active. */
//        "activeCaptionText", "#FFFFFF", /* Text color for text in captions (title bars). */
//      "activeCaptionBorder", "#B24D7A", /* Border color for caption (title bar) window borders. */
//          "inactiveCaption", "#AEB2C3", /* Color for captions (title bars) when not active. */
//      "inactiveCaptionText", "#000000", /* Text color for text in inactive captions (title bars). */
//    "inactiveCaptionBorder", "#AEB2C3", /* Border color for inactive caption (title bar) window borders. */
//                   "window", "#AEB2C3", /* Default color for the interior of windows */
//             "windowBorder", "#AEB2C3", /* ??? */
//               "windowText", "#000000", /* ??? */
//                     "menu", "#AEB2C3", /* ??? */
//                 "menuText", "#000000", /* ??? */
//                     "text", "#FFFFFF", /* Text background color */
//                 "textText", "#000000", /* Text foreground color */
//            "textHighlight", "#000000", /* Text background color when selected */
//        "textHighlightText", "#FFF7E9", /* Text color when selected */
//         "textInactiveText", "#808080", /* Text color when disabled */
//                  "control", "#ff0000", /* Default color for controls (buttons, sliders, etc) */
//              "controlText", "#000000", /* Default color for text in controls */
//         "controlHighlight", "#DCDEE5", /* Highlight color for controls */
//       "controlLtHighlight", "#DCDEE5", /* Light highlight color for controls */
//            "controlShadow", "#63656F", /* Shadow color for controls */
//       "controlLightShadow", "#9397A5", /* Shadow color for controls */           
//          "controlDkShadow", "#000000", /* Dark shadow color for controls */
//                "scrollbar", "#AEB2C3", /* Scrollbar ??? color. PENDING(jeff) foreground? background? ?*/
//                     "info", "#FFF7E9", /* ??? */
//                 "infoText", "#000000"  /* ??? */
        };

        // AWT SystemColors only for for CDE on JDK1.2
        loadSystemColors(table, defaultSystemColors, false/*is1dot2*/);
    }

    @Override
	protected void initComponentDefaults(UIDefaults table)
    {
//    	super.initComponentDefaults(table);
    	table.put("ComboBox.border", null);
//    	table.put("MenuBar.border", BorderFactory.createLineBorder(table.getColor("MenuBar.borderColor"), table.getInt("MenuBar.borderSize")));
    	table.put("Button.border", BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), BorderFactory.createEmptyBorder(2, 3, 2, 3)));
    	table.put("ToggleButton.border", BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), BorderFactory.createEmptyBorder(2, 3, 2, 3)));
    	table.put("ProgressBar.border", BorderFactory.createEtchedBorder());
    	table.put("TableHeader.cellBorder", BorderFactory.createLineBorder(table.getColor("TableHeader.borderColor"), table.getInt("TableHeader.borderSize")));
//    	table.put("Table.cellBorder", null);
    	table.put("Table.focusCellHighlightBorder", null);
        table.put("Tree.openIcon", new ImageIcon(MotifLookAndFeel.class.getResource("icons/TreeOpen.gif")));
        table.put("Tree.closedIcon", new ImageIcon(MotifLookAndFeel.class.getResource("icons/TreeClosed.gif")));
        table.put("Tree.leafIcon", new UIDefaults.LazyValue() { public Object createValue(UIDefaults table) { return MotifTreeCellRenderer.loadLeafIcon(); } });
//        table.put("Tree.openIcon", new UIDefaults.LazyValue() { public Object createValue(UIDefaults table) { return UIResourceManager.getIcon("folder.png"); } });
//        table.put("Tree.closedIcon", new UIDefaults.LazyValue() { public Object createValue(UIDefaults table) { return UIResourceManager.getIcon("folder.png"); } });
        table.put("Tree.expandedIcon", new UIDefaults.LazyValue() { public Object createValue(UIDefaults table) { return UIResourceManager.getIcon("arrow_down.png"); } });
        table.put("Tree.collapsedIcon", new UIDefaults.LazyValue() { public Object createValue(UIDefaults table) { return UIResourceManager.getIcon("arrow_right.png"); } });

        // this doesn't compeltely fix it. KeyEvent.getmodifierskeytext has the + hardcoded!
        if (Platform.isMacOS())
        	table.put("MenuItem.acceleratorDelimiter", "");        
    }
	@Override
	public String getDescription() {
		return "Korsakow Look And Feel";
	}
	@Override
	public String getID() {
		return "KorsakowLookAndFeel";
	}
	@Override
	public String getName() {
		return "KorsakowLookAndFeel";
	}
	@Override
	public boolean isNativeLookAndFeel() {
		return false;
	}
	@Override
	public boolean isSupportedLookAndFeel() {
		return true;
	}
}
