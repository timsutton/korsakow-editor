package org.korsakow.ide.ui.laf;

import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.text.DefaultEditorKit;

import org.korsakow.ide.util.Platform;

public class KorsakowDefaultKeybindings
{
	public static void installKeybindings(UIDefaults table)
	{
		String command = Platform.isMacOS()?"meta":"ctrl";

	    table.put("Desktop.ancestorInputMap",
	       new UIDefaults.LazyInputMap(new Object[] {
		 ""+command+" F5", "restore", 
		 ""+command+" F4", "close",
		 ""+command+" F7", "move", 
		 ""+command+" F8", "resize",
		   "RIGHT", "right",
		"KP_RIGHT", "right",
             "shift RIGHT", "shrinkRight",
          "shift KP_RIGHT", "shrinkRight",
		    "LEFT", "left",
		 "KP_LEFT", "left",
              "shift LEFT", "shrinkLeft",
           "shift KP_LEFT", "shrinkLeft",
		      "UP", "up",
		   "KP_UP", "up",
                "shift UP", "shrinkUp",
             "shift KP_UP", "shrinkUp",
		    "DOWN", "down",
		 "KP_DOWN", "down",
              "shift DOWN", "shrinkDown",
           "shift KP_DOWN", "shrinkDown",
		  "ESCAPE", "escape",
		 ""+command+" F9", "minimize", 
		""+command+" F10", "maximize",
		 ""+command+" F6", "selectNextFrame",
		""+command+" TAB", "selectNextFrame",
	     ""+command+" alt F6", "selectNextFrame",
       "shift "+command+" alt F6", "selectPreviousFrame",
                ""+command+" F12", "navigateNext",
          "shift "+command+" F12", "navigatePrevious"
	      }));

	    // *** Label
	    table.put(
	    "List.focusInputMap",
	       new UIDefaults.LazyInputMap(new Object[] {
                           ""+command+" C", "copy",
                           ""+command+" V", "paste",
                           ""+command+" X", "cut",
                             "COPY", "copy",
                            "PASTE", "paste",
                              "CUT", "cut",
		               "UP", "selectPreviousRow",
		            "KP_UP", "selectPreviousRow",
		         "shift UP", "selectPreviousRowExtendSelection",
		      "shift KP_UP", "selectPreviousRowExtendSelection",
                    ""+command+" shift UP", "selectPreviousRowExtendSelection",
                 ""+command+" shift KP_UP", "selectPreviousRowExtendSelection",
                          ""+command+" UP", "selectPreviousRowChangeLead",
                       ""+command+" KP_UP", "selectPreviousRowChangeLead",
		             "DOWN", "selectNextRow",
		          "KP_DOWN", "selectNextRow",
		       "shift DOWN", "selectNextRowExtendSelection",
		    "shift KP_DOWN", "selectNextRowExtendSelection",
                  ""+command+" shift DOWN", "selectNextRowExtendSelection",
               ""+command+" shift KP_DOWN", "selectNextRowExtendSelection",
                        ""+command+" DOWN", "selectNextRowChangeLead",
                     ""+command+" KP_DOWN", "selectNextRowChangeLead",
		             "LEFT", "selectPreviousColumn",
		          "KP_LEFT", "selectPreviousColumn",
		       "shift LEFT", "selectPreviousColumnExtendSelection",
		    "shift KP_LEFT", "selectPreviousColumnExtendSelection",
                  ""+command+" shift LEFT", "selectPreviousColumnExtendSelection",
               ""+command+" shift KP_LEFT", "selectPreviousColumnExtendSelection",
                        ""+command+" LEFT", "selectPreviousColumnChangeLead",
                     ""+command+" KP_LEFT", "selectPreviousColumnChangeLead",
		            "RIGHT", "selectNextColumn",
		         "KP_RIGHT", "selectNextColumn",
		      "shift RIGHT", "selectNextColumnExtendSelection",
		   "shift KP_RIGHT", "selectNextColumnExtendSelection",
                 ""+command+" shift RIGHT", "selectNextColumnExtendSelection",
              ""+command+" shift KP_RIGHT", "selectNextColumnExtendSelection",
                       ""+command+" RIGHT", "selectNextColumnChangeLead",
                    ""+command+" KP_RIGHT", "selectNextColumnChangeLead",
		             "HOME", "selectFirstRow",
		       "shift HOME", "selectFirstRowExtendSelection",
                  ""+command+" shift HOME", "selectFirstRowExtendSelection",
                        ""+command+" HOME", "selectFirstRowChangeLead",
		              "END", "selectLastRow",
		        "shift END", "selectLastRowExtendSelection",
                   ""+command+" shift END", "selectLastRowExtendSelection",
                         ""+command+" END", "selectLastRowChangeLead",
		          "PAGE_UP", "scrollUp",
		    "shift PAGE_UP", "scrollUpExtendSelection",
               ""+command+" shift PAGE_UP", "scrollUpExtendSelection",
                     ""+command+" PAGE_UP", "scrollUpChangeLead",
		        "PAGE_DOWN", "scrollDown",
		  "shift PAGE_DOWN", "scrollDownExtendSelection",
             ""+command+" shift PAGE_DOWN", "scrollDownExtendSelection",
                   ""+command+" PAGE_DOWN", "scrollDownChangeLead",
		           ""+command+" A", "selectAll",
		       ""+command+" SLASH", "selectAll",
		  ""+command+" BACK_SLASH", "clearSelection",
                            "SPACE", "addToSelection",
                       ""+command+" SPACE", "toggleAndAnchor",
                      "shift SPACE", "extendTo",
                 ""+command+" shift SPACE", "moveSelectionTo"
		 }));
	    table.put(
	    "List.focusInputMap.RightToLeft",
	       new UIDefaults.LazyInputMap(new Object[] {
		             "LEFT", "selectNextColumn",
		          "KP_LEFT", "selectNextColumn",
		       "shift LEFT", "selectNextColumnExtendSelection",
		    "shift KP_LEFT", "selectNextColumnExtendSelection",
                  ""+command+" shift LEFT", "selectNextColumnExtendSelection",
               ""+command+" shift KP_LEFT", "selectNextColumnExtendSelection",
                        ""+command+" LEFT", "selectNextColumnChangeLead",
                     ""+command+" KP_LEFT", "selectNextColumnChangeLead",
		            "RIGHT", "selectPreviousColumn",
		         "KP_RIGHT", "selectPreviousColumn",
		      "shift RIGHT", "selectPreviousColumnExtendSelection",
		   "shift KP_RIGHT", "selectPreviousColumnExtendSelection",
                 ""+command+" shift RIGHT", "selectPreviousColumnExtendSelection",
              ""+command+" shift KP_RIGHT", "selectPreviousColumnExtendSelection",
                       ""+command+" RIGHT", "selectPreviousColumnChangeLead",
                    ""+command+" KP_RIGHT", "selectPreviousColumnChangeLead",
		 }));

	    // These window InputMap bindings are used when the Menu is
	    // selected.
	    table.put(
	    "PopupMenu.selectedWindowInputMapBindings", new Object[] {
		  "ESCAPE", "cancel",
                    "DOWN", "selectNext",
		 "KP_DOWN", "selectNext",
		      "UP", "selectPrevious",
		   "KP_UP", "selectPrevious",
		    "LEFT", "selectParent",
		 "KP_LEFT", "selectParent",
		   "RIGHT", "selectChild",
		"KP_RIGHT", "selectChild",
		   "ENTER", "return",
		   "SPACE", "return"
	    });
	    table.put(
	    "PopupMenu.selectedWindowInputMapBindings.RightToLeft", new Object[] {
		    "LEFT", "selectChild",
		 "KP_LEFT", "selectChild",
		   "RIGHT", "selectParent",
		"KP_RIGHT", "selectParent",
	    });
	    
	    table.put(
	    "ScrollBar.ancestorInputMap",
	       new UIDefaults.LazyInputMap(new Object[] {
		       "RIGHT", "positiveUnitIncrement",
		    "KP_RIGHT", "positiveUnitIncrement",
		        "DOWN", "positiveUnitIncrement",
		     "KP_DOWN", "positiveUnitIncrement",
		   "PAGE_DOWN", "positiveBlockIncrement",
		        "LEFT", "negativeUnitIncrement",
		     "KP_LEFT", "negativeUnitIncrement",
		          "UP", "negativeUnitIncrement",
		       "KP_UP", "negativeUnitIncrement",
		     "PAGE_UP", "negativeBlockIncrement",
		        "HOME", "minScroll",
		         "END", "maxScroll"
		 }));
	    table.put(
	    "ScrollBar.ancestorInputMap.RightToLeft",
	       new UIDefaults.LazyInputMap(new Object[] {
		       "RIGHT", "negativeUnitIncrement",
		    "KP_RIGHT", "negativeUnitIncrement",
		        "LEFT", "positiveUnitIncrement",
		     "KP_LEFT", "positiveUnitIncrement",
		 }));
	    table.put(
	    "ScrollPane.ancestorInputMap",
	       new UIDefaults.LazyInputMap(new Object[] {
// Fix for #1329. Unfortunately I could not figure out how to simply disable the one textfield!
//		           "RIGHT", "unitScrollRight",
//		        "KP_RIGHT", "unitScrollRight",
//		            "DOWN", "unitScrollDown",
//		         "KP_DOWN", "unitScrollDown",
//		            "LEFT", "unitScrollLeft",
//		         "KP_LEFT", "unitScrollLeft",
//		              "UP", "unitScrollUp",
//		           "KP_UP", "unitScrollUp",
		         "PAGE_UP", "scrollUp",
		       "PAGE_DOWN", "scrollDown",
		    ""+command+" PAGE_UP", "scrollLeft",
		  ""+command+" PAGE_DOWN", "scrollRight",
		       ""+command+" HOME", "scrollHome",
		        ""+command+" END", "scrollEnd"
		 }));
	    table.put(
	    "ScrollPane.ancestorInputMap.RightToLeft",
	       new UIDefaults.LazyInputMap(new Object[] {
		    ""+command+" PAGE_UP", "scrollRight",
		  ""+command+" PAGE_DOWN", "scrollLeft",
		 }));
	    table.put(
	    "Slider.focusInputMap",
	       new UIDefaults.LazyInputMap(new Object[] {
		       "RIGHT", "positiveUnitIncrement",
		    "KP_RIGHT", "positiveUnitIncrement",
		        "DOWN", "negativeUnitIncrement",
		     "KP_DOWN", "negativeUnitIncrement",
		   "PAGE_DOWN", "negativeBlockIncrement",
		        "LEFT", "negativeUnitIncrement",
		     "KP_LEFT", "negativeUnitIncrement",
		          "UP", "positiveUnitIncrement",
		       "KP_UP", "positiveUnitIncrement",
		     "PAGE_UP", "positiveBlockIncrement",
		        "HOME", "minScroll",
		         "END", "maxScroll"
		 }));
	    table.put(
	    "Slider.focusInputMap.RightToLeft",
	       new UIDefaults.LazyInputMap(new Object[] {
		       "RIGHT", "negativeUnitIncrement",
		    "KP_RIGHT", "negativeUnitIncrement",
		        "LEFT", "positiveUnitIncrement",
		     "KP_LEFT", "positiveUnitIncrement",
		 }));

	    // *** Spinner
	    table.put(
            "Spinner.ancestorInputMap",
	       new UIDefaults.LazyInputMap(new Object[] {
                               "UP", "increment",
                            "KP_UP", "increment",
                             "DOWN", "decrement",
                          "KP_DOWN", "decrement",
               }));
	    table.put(
	    "SplitPane.ancestorInputMap",
	       new UIDefaults.LazyInputMap(new Object[] {
		        "UP", "negativeIncrement",
		      "DOWN", "positiveIncrement",
		      "LEFT", "negativeIncrement",
		     "RIGHT", "positiveIncrement",
		     "KP_UP", "negativeIncrement",
		   "KP_DOWN", "positiveIncrement",
		   "KP_LEFT", "negativeIncrement",
		  "KP_RIGHT", "positiveIncrement",
		      "HOME", "selectMin",
		       "END", "selectMax",
		        "F8", "startResize",
		        "F6", "toggleFocus",
		  ""+command+" TAB", "focusOutForward",
 	    ""+command+" shift TAB", "focusOutBackward"
		 }));

	    table.put(
	    "TabbedPane.focusInputMap",
	      new UIDefaults.LazyInputMap(new Object[] {
		         "RIGHT", "navigateRight",
	              "KP_RIGHT", "navigateRight",
	                  "LEFT", "navigateLeft",
	               "KP_LEFT", "navigateLeft",
	                    "UP", "navigateUp",
	                 "KP_UP", "navigateUp",
	                  "DOWN", "navigateDown",
	               "KP_DOWN", "navigateDown",
	             ""+command+" DOWN", "requestFocusForVisibleComponent",
	          ""+command+" KP_DOWN", "requestFocusForVisibleComponent",
		}));
	    table.put(
	    "TabbedPane.ancestorInputMap",
	       new UIDefaults.LazyInputMap(new Object[] {
		   ""+command+" PAGE_DOWN", "navigatePageDown",
	             ""+command+" PAGE_UP", "navigatePageUp",
	                  ""+command+" UP", "requestFocus",
	               ""+command+" KP_UP", "requestFocus",
		 }));


	    table.put(
	    "Table.ancestorInputMap",
	       new UIDefaults.LazyInputMap(new Object[] {
//                               ""+command+" C", "copy",
//                               ""+command+" V", "paste",
//                               ""+command+" X", "cut",
//                                 "COPY", "copy",
//                                "PASTE", "paste",
//                                  "CUT", "cut",
                                "RIGHT", "selectNextColumn",
                             "KP_RIGHT", "selectNextColumn",
                          "shift RIGHT", "selectNextColumnExtendSelection",
                       "shift KP_RIGHT", "selectNextColumnExtendSelection",
                     ""+command+" shift RIGHT", "selectNextColumnExtendSelection",
                  ""+command+" shift KP_RIGHT", "selectNextColumnExtendSelection",
                           ""+command+" RIGHT", "selectNextColumnChangeLead",
                        ""+command+" KP_RIGHT", "selectNextColumnChangeLead",
                                 "LEFT", "selectPreviousColumn",
                              "KP_LEFT", "selectPreviousColumn",
                           "shift LEFT", "selectPreviousColumnExtendSelection",
                        "shift KP_LEFT", "selectPreviousColumnExtendSelection",
                      ""+command+" shift LEFT", "selectPreviousColumnExtendSelection",
                   ""+command+" shift KP_LEFT", "selectPreviousColumnExtendSelection",
                            ""+command+" LEFT", "selectPreviousColumnChangeLead",
                         ""+command+" KP_LEFT", "selectPreviousColumnChangeLead",
                                 "DOWN", "selectNextRow",
                              "KP_DOWN", "selectNextRow",
                           "shift DOWN", "selectNextRowExtendSelection",
                        "shift KP_DOWN", "selectNextRowExtendSelection",
                      ""+command+" shift DOWN", "selectNextRowExtendSelection",
                   ""+command+" shift KP_DOWN", "selectNextRowExtendSelection",
                            ""+command+" DOWN", "selectNextRowChangeLead",
                         ""+command+" KP_DOWN", "selectNextRowChangeLead",
                                   "UP", "selectPreviousRow",
                                "KP_UP", "selectPreviousRow",
                             "shift UP", "selectPreviousRowExtendSelection",
                          "shift KP_UP", "selectPreviousRowExtendSelection",
                        ""+command+" shift UP", "selectPreviousRowExtendSelection",
                     ""+command+" shift KP_UP", "selectPreviousRowExtendSelection",
                              ""+command+" UP", "selectPreviousRowChangeLead",
                           ""+command+" KP_UP", "selectPreviousRowChangeLead",
                                 "HOME", "selectFirstColumn",
                           "shift HOME", "selectFirstColumnExtendSelection",
                      ""+command+" shift HOME", "selectFirstRowExtendSelection",
                            ""+command+" HOME", "selectFirstRow",
                                  "END", "selectLastColumn",
                            "shift END", "selectLastColumnExtendSelection",
                       ""+command+" shift END", "selectLastRowExtendSelection",
                             ""+command+" END", "selectLastRow",
                              "PAGE_UP", "scrollUpChangeSelection",
                        "shift PAGE_UP", "scrollUpExtendSelection",
                   ""+command+" shift PAGE_UP", "scrollLeftExtendSelection",
                         ""+command+" PAGE_UP", "scrollLeftChangeSelection",
                            "PAGE_DOWN", "scrollDownChangeSelection",
                      "shift PAGE_DOWN", "scrollDownExtendSelection",
                 ""+command+" shift PAGE_DOWN", "scrollRightExtendSelection",
                       ""+command+" PAGE_DOWN", "scrollRightChangeSelection",
                                  "TAB", "selectNextColumnCell",
                            "shift TAB", "selectPreviousColumnCell",
//                                "ENTER", "selectNextRowCell",
                          "shift ENTER", "selectPreviousRowCell",
                               ""+command+" A", "selectAll",
                           ""+command+" SLASH", "selectAll",
                      ""+command+" BACK_SLASH", "clearSelection",
                               "ESCAPE", "cancel",
                                   "F2", "startEditing",
                                "SPACE", "addToSelection",
                           ""+command+" SPACE", "toggleAndAnchor",
                          "shift SPACE", "extendTo",
                     ""+command+" shift SPACE", "moveSelectionTo"
		 }));
	    table.put(
	    "Table.ancestorInputMap.RightToLeft",
	       new UIDefaults.LazyInputMap(new Object[] {
		                "RIGHT", "selectPreviousColumn",
		             "KP_RIGHT", "selectPreviousColumn",
                          "shift RIGHT", "selectPreviousColumnExtendSelection",
                       "shift KP_RIGHT", "selectPreviousColumnExtendSelection",
                     ""+command+" shift RIGHT", "selectPreviousColumnExtendSelection",
                  ""+command+" shift KP_RIGHT", "selectPreviousColumnExtendSelection",
                          "shift RIGHT", "selectPreviousColumnChangeLead",
                       "shift KP_RIGHT", "selectPreviousColumnChangeLead",
		                 "LEFT", "selectNextColumn",
		              "KP_LEFT", "selectNextColumn",
		           "shift LEFT", "selectNextColumnExtendSelection",
		        "shift KP_LEFT", "selectNextColumnExtendSelection",
                      ""+command+" shift LEFT", "selectNextColumnExtendSelection",
                   ""+command+" shift KP_LEFT", "selectNextColumnExtendSelection",
                            ""+command+" LEFT", "selectNextColumnChangeLead",
                         ""+command+" KP_LEFT", "selectNextColumnChangeLead",
		         ""+command+" PAGE_UP", "scrollRightChangeSelection",
		       ""+command+" PAGE_DOWN", "scrollLeftChangeSelection",
		   ""+command+" shift PAGE_UP", "scrollRightExtendSelection",
		 ""+command+" shift PAGE_DOWN", "scrollLeftExtendSelection",
		 }));


	    UIDefaults.LazyInputMap textFieldFocusInputMap =
        new UIDefaults.LazyInputMap(new Object[] {
                     ""+command+" C", DefaultEditorKit.copyAction,
                     ""+command+" V", DefaultEditorKit.pasteAction,
                     ""+command+" X", DefaultEditorKit.cutAction,
                       "COPY", DefaultEditorKit.copyAction,
                      "PASTE", DefaultEditorKit.pasteAction,
                        "CUT", DefaultEditorKit.cutAction,
                 "shift LEFT", DefaultEditorKit.selectionBackwardAction,
              "shift KP_LEFT", DefaultEditorKit.selectionBackwardAction,
                "shift RIGHT", DefaultEditorKit.selectionForwardAction,
             "shift KP_RIGHT", DefaultEditorKit.selectionForwardAction,
                  ""+command+" LEFT", DefaultEditorKit.previousWordAction,
               ""+command+" KP_LEFT", DefaultEditorKit.previousWordAction,
                 ""+command+" RIGHT", DefaultEditorKit.nextWordAction,
              ""+command+" KP_RIGHT", DefaultEditorKit.nextWordAction,
            ""+command+" shift LEFT", DefaultEditorKit.selectionPreviousWordAction,
         ""+command+" shift KP_LEFT", DefaultEditorKit.selectionPreviousWordAction,
           ""+command+" shift RIGHT", DefaultEditorKit.selectionNextWordAction,
        ""+command+" shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction,
                     ""+command+" A", DefaultEditorKit.selectAllAction,
                       "HOME", DefaultEditorKit.beginLineAction,
                        "END", DefaultEditorKit.endLineAction,
                 "shift HOME", DefaultEditorKit.selectionBeginLineAction,
                  "shift END", DefaultEditorKit.selectionEndLineAction,
                 "BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
                     ""+command+" H", DefaultEditorKit.deletePrevCharAction,
                     "DELETE", DefaultEditorKit.deleteNextCharAction,
                      "RIGHT", DefaultEditorKit.forwardAction,
                       "LEFT", DefaultEditorKit.backwardAction,
                   "KP_RIGHT", DefaultEditorKit.forwardAction,
                    "KP_LEFT", DefaultEditorKit.backwardAction,
                      "ENTER", JTextField.notifyAction,
            ""+command+" BACK_SLASH", "unselect",
            "control shift O", "toggle-componentOrientation",
                     "ESCAPE", "reset-field-edit",
                         "UP", "increment",
                      "KP_UP", "increment",
                       "DOWN", "decrement",
                    "KP_DOWN", "decrement",
        });
	    table.put("TextField.focusInputMap", textFieldFocusInputMap);
	    table.put("TextArea.focusInputMap", textFieldFocusInputMap);
	    table.put(
	    "FormattedTextField.focusInputMap",
              new UIDefaults.LazyInputMap(new Object[] {
                           ""+command+" C", DefaultEditorKit.copyAction,
                           ""+command+" V", DefaultEditorKit.pasteAction,
                           ""+command+" X", DefaultEditorKit.cutAction,
                             "COPY", DefaultEditorKit.copyAction,
                            "PASTE", DefaultEditorKit.pasteAction,
                              "CUT", DefaultEditorKit.cutAction,
                       "shift LEFT", DefaultEditorKit.selectionBackwardAction,
                    "shift KP_LEFT", DefaultEditorKit.selectionBackwardAction,
                      "shift RIGHT", DefaultEditorKit.selectionForwardAction,
                   "shift KP_RIGHT", DefaultEditorKit.selectionForwardAction,
                        ""+command+" LEFT", DefaultEditorKit.previousWordAction,
                     ""+command+" KP_LEFT", DefaultEditorKit.previousWordAction,
                       ""+command+" RIGHT", DefaultEditorKit.nextWordAction,
                    ""+command+" KP_RIGHT", DefaultEditorKit.nextWordAction,
                  ""+command+" shift LEFT", DefaultEditorKit.selectionPreviousWordAction,
               ""+command+" shift KP_LEFT", DefaultEditorKit.selectionPreviousWordAction,
                 ""+command+" shift RIGHT", DefaultEditorKit.selectionNextWordAction,
              ""+command+" shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction,
                           ""+command+" A", DefaultEditorKit.selectAllAction,
                             "HOME", DefaultEditorKit.beginLineAction,
                              "END", DefaultEditorKit.endLineAction,
                       "shift HOME", DefaultEditorKit.selectionBeginLineAction,
                        "shift END", DefaultEditorKit.selectionEndLineAction,
                       "BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
                           ""+command+" H", DefaultEditorKit.deletePrevCharAction,
                           "DELETE", DefaultEditorKit.deleteNextCharAction,
                            "RIGHT", DefaultEditorKit.forwardAction,
                             "LEFT", DefaultEditorKit.backwardAction,
                         "KP_RIGHT", DefaultEditorKit.forwardAction,
                          "KP_LEFT", DefaultEditorKit.backwardAction,
//                            "ENTER", JTextField.notifyAction,
                  ""+command+" BACK_SLASH", "unselect",
                  "control shift O", "toggle-componentOrientation",
                           "ESCAPE", "reset-field-edit",
                               "UP", "increment",
                            "KP_UP", "increment",
                             "DOWN", "decrement",
                          "KP_DOWN", "decrement",
              }));

	    table.put(
	    "ToolBar.ancestorInputMap",
	       new UIDefaults.LazyInputMap(new Object[] {
		        "UP", "navigateUp",
		     "KP_UP", "navigateUp",
		      "DOWN", "navigateDown",
		   "KP_DOWN", "navigateDown",
		      "LEFT", "navigateLeft",
		   "KP_LEFT", "navigateLeft",
		     "RIGHT", "navigateRight",
		  "KP_RIGHT", "navigateRight"
		 }));

	    table.put(
	    "Tree.focusInputMap",
	       new UIDefaults.LazyInputMap(new Object[] {
                                 ""+command+" C", "copy",
                                 ""+command+" V", "paste",
                                 ""+command+" X", "cut",
                                   "COPY", "copy",
                                  "PASTE", "paste",
                                    "CUT", "cut",
		                     "UP", "selectPrevious",
		                  "KP_UP", "selectPrevious",
		               "shift UP", "selectPreviousExtendSelection",
		            "shift KP_UP", "selectPreviousExtendSelection",
                          ""+command+" shift UP", "selectPreviousExtendSelection",
                       ""+command+" shift KP_UP", "selectPreviousExtendSelection",
                                ""+command+" UP", "selectPreviousChangeLead",
                             ""+command+" KP_UP", "selectPreviousChangeLead",
		                   "DOWN", "selectNext",
		                "KP_DOWN", "selectNext",
		             "shift DOWN", "selectNextExtendSelection",
		          "shift KP_DOWN", "selectNextExtendSelection",
                        ""+command+" shift DOWN", "selectNextExtendSelection",
                     ""+command+" shift KP_DOWN", "selectNextExtendSelection",
                              ""+command+" DOWN", "selectNextChangeLead",
                           ""+command+" KP_DOWN", "selectNextChangeLead",
		                  "RIGHT", "selectChild",
		               "KP_RIGHT", "selectChild",
		                   "LEFT", "selectParent",
		                "KP_LEFT", "selectParent",
		                "PAGE_UP", "scrollUpChangeSelection",
		          "shift PAGE_UP", "scrollUpExtendSelection",
                     ""+command+" shift PAGE_UP", "scrollUpExtendSelection",
                           ""+command+" PAGE_UP", "scrollUpChangeLead",
		              "PAGE_DOWN", "scrollDownChangeSelection",
		        "shift PAGE_DOWN", "scrollDownExtendSelection",
                   ""+command+" shift PAGE_DOWN", "scrollDownExtendSelection",
                         ""+command+" PAGE_DOWN", "scrollDownChangeLead",
		                   "HOME", "selectFirst",
		             "shift HOME", "selectFirstExtendSelection",
                        ""+command+" shift HOME", "selectFirstExtendSelection",
                              ""+command+" HOME", "selectFirstChangeLead",
		                    "END", "selectLast",
		              "shift END", "selectLastExtendSelection",
                         ""+command+" shift END", "selectLastExtendSelection",
                               ""+command+" END", "selectLastChangeLead",
		                     "F2", "startEditing",
		                 ""+command+" A", "selectAll",
		             ""+command+" SLASH", "selectAll",
		        ""+command+" BACK_SLASH", "clearSelection",
		              ""+command+" LEFT", "scrollLeft",
		           ""+command+" KP_LEFT", "scrollLeft",
		             ""+command+" RIGHT", "scrollRight",
		          ""+command+" KP_RIGHT", "scrollRight",
                                  "SPACE", "addToSelection",
                             ""+command+" SPACE", "toggleAndAnchor",
                            "shift SPACE", "extendTo",
                       ""+command+" shift SPACE", "moveSelectionTo"
		 }));
	    table.put(
	    "Tree.focusInputMap.RightToLeft",
	       new UIDefaults.LazyInputMap(new Object[] {
		                  "RIGHT", "selectParent",
		               "KP_RIGHT", "selectParent",
		                   "LEFT", "selectChild",
		                "KP_LEFT", "selectChild",
		 }));
	    table.put(
	    "Tree.ancestorInputMap",
	       new UIDefaults.LazyInputMap(new Object[] {
		     "ESCAPE", "cancel"
		 }));
	    table.put(
            // Bind specific keys that can invoke popup on currently
            // focused JComponent
            "RootPane.ancestorInputMap",
                new UIDefaults.LazyInputMap(new Object[] {
                     "shift F10", "postPopup",
                  }));

	    // These bindings are only enabled when there is a default
	    // button set on the rootpane.
	    table.put(
	    "RootPane.defaultButtonWindowKeyBindings", new Object[] {
		             "ENTER", "press",
		    "released ENTER", "release",
		        ""+command+" ENTER", "press",
	       ""+command+" released ENTER", "release"
	      });
	}
}
