@SET FILE=%1
REM /n opens the tree view on the side
REM /select selects the file
REM notice how we don't qualify explorer's path. firstly explorer should always be in the path and it does happen (albeit rarely) that windows doesn't live in "c:\windows"
REM calling explorer from the CMD line has been known to fail in some unofficial "optimized" builds of windows (ie mine), but should otherwise always work
@explorer /n,/select,"%FILE%"
