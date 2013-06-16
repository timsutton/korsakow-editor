package org.korsakow.font
{
	public interface IFontLibrary
	{
		function get fonts():Array;
		function get fontNames():Array;
		function getFont(familyName:String):Class;
		function hasFont(familyName:String):Boolean;
	}
}
