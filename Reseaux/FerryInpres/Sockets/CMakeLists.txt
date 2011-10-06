find_package (Threads)

add_library (
    Sockets ClientSocket.cpp ServerSocket.cpp Exception.cpp SocketException.cpp
    SocketsUtils.cpp
)
target_link_libraries (Sockets ${CMAKE_THREAD_LIBS_INIT})