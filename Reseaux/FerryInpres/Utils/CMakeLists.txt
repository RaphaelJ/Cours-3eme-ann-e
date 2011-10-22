find_package (Threads)

add_library (
    Utils Time.cpp Async.cpp Exception.cpp
)
