project (ferryinpres)

cmake_minimum_required(VERSION 2.8)

include_directories ("${PROJECT_SOURCE_DIR}")

add_subdirectory (Utils)
add_subdirectory (Sockets)
add_subdirectory (IniParser)
add_subdirectory (ThreadPool)

add_executable (serveur Server.cpp TerminalServer.cpp) 
target_link_libraries (serveur Sockets Utils IniParser)

add_executable (client Client.cpp) 
target_link_libraries (client Sockets)

set (CMAKE_CXX_FLAGS "-g -Wall")