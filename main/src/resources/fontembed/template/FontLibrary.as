package org.korsakow.font
{
	import flash.display.Sprite;
	[SWF(width="1", height="1", frameRate="1")]
	public class ${className} extends Sprite implements IFontLibrary
	{
		public function get fonts():Array
		{
			return _fontsList.concat();
		}
		public function get fontNames():Array
		{
			var names:Array = new Array();
			for (var name:String in _fontsByName)
				names.push(name);
			return names;
		}
		public function getFont(familyName:String):Class
		{
			return _fontsByName[familyName] as Class;
		}
		public function hasFont(familyName:String):Boolean
		{
			return _fontsByName.hasOwnProperty(familyName);
		}
		
		private var _fontsByName:Object = {};
		private var _fontsList:Array = [];
		
		// font vars
${fontVars}
		
		// constructor
		public function ${className}()
		{
${fontEntries}
		}
	}
}
