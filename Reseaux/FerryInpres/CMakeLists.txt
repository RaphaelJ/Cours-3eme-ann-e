project (ferryinpres)

include_directories ("${PROJECT_SOURCE_DIR}/Sockets")
add_subdirectory (Sockets) 

add_executable (serveur server.cpp) 
target_link_libraries (serveur Sockets)

add_executable (client client.cpp) 
target_link_libraries (client Sockets)

set (CMAKE_CXX_FLAGS "-g -Wall")