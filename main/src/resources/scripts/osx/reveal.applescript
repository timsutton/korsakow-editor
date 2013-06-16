
on run arg
	#	repeat with arg in argv
	set theFile to the POSIX file arg
	
	
	tell application "Finder"
		activate
		reveal theFile
	end tell
	
	#	end repeat
	
end run
