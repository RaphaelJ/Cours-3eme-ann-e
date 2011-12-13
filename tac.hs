import System.Environment
main = do
	fn <- getArgs 
	f <- readFile $ fn !! 0
	print $ unlines $ reverse $ lines f
