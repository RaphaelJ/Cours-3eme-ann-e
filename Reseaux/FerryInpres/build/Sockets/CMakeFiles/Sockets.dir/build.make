# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 2.8

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canoncical targets will work.
.SUFFIXES:

# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list

# Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E remove -f

# The program to use to edit the cache.
CMAKE_EDIT_COMMAND = /usr/bin/ccmake

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/rapha/Desktop/Cours/Reseaux/FerryInpres

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build

# Include any dependencies generated for this target.
include Sockets/CMakeFiles/Sockets.dir/depend.make

# Include the progress variables for this target.
include Sockets/CMakeFiles/Sockets.dir/progress.make

# Include the compile flags for this target's objects.
include Sockets/CMakeFiles/Sockets.dir/flags.make

Sockets/CMakeFiles/Sockets.dir/ClientSocket.cpp.o: Sockets/CMakeFiles/Sockets.dir/flags.make
Sockets/CMakeFiles/Sockets.dir/ClientSocket.cpp.o: ../Sockets/ClientSocket.cpp
	$(CMAKE_COMMAND) -E cmake_progress_report /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/CMakeFiles $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building CXX object Sockets/CMakeFiles/Sockets.dir/ClientSocket.cpp.o"
	cd /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/Sockets && /usr/bin/c++   $(CXX_DEFINES) $(CXX_FLAGS) -o CMakeFiles/Sockets.dir/ClientSocket.cpp.o -c /home/rapha/Desktop/Cours/Reseaux/FerryInpres/Sockets/ClientSocket.cpp

Sockets/CMakeFiles/Sockets.dir/ClientSocket.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/Sockets.dir/ClientSocket.cpp.i"
	cd /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/Sockets && /usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -E /home/rapha/Desktop/Cours/Reseaux/FerryInpres/Sockets/ClientSocket.cpp > CMakeFiles/Sockets.dir/ClientSocket.cpp.i

Sockets/CMakeFiles/Sockets.dir/ClientSocket.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/Sockets.dir/ClientSocket.cpp.s"
	cd /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/Sockets && /usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -S /home/rapha/Desktop/Cours/Reseaux/FerryInpres/Sockets/ClientSocket.cpp -o CMakeFiles/Sockets.dir/ClientSocket.cpp.s

Sockets/CMakeFiles/Sockets.dir/ClientSocket.cpp.o.requires:
.PHONY : Sockets/CMakeFiles/Sockets.dir/ClientSocket.cpp.o.requires

Sockets/CMakeFiles/Sockets.dir/ClientSocket.cpp.o.provides: Sockets/CMakeFiles/Sockets.dir/ClientSocket.cpp.o.requires
	$(MAKE) -f Sockets/CMakeFiles/Sockets.dir/build.make Sockets/CMakeFiles/Sockets.dir/ClientSocket.cpp.o.provides.build
.PHONY : Sockets/CMakeFiles/Sockets.dir/ClientSocket.cpp.o.provides

Sockets/CMakeFiles/Sockets.dir/ClientSocket.cpp.o.provides.build: Sockets/CMakeFiles/Sockets.dir/ClientSocket.cpp.o

Sockets/CMakeFiles/Sockets.dir/ServerSocket.cpp.o: Sockets/CMakeFiles/Sockets.dir/flags.make
Sockets/CMakeFiles/Sockets.dir/ServerSocket.cpp.o: ../Sockets/ServerSocket.cpp
	$(CMAKE_COMMAND) -E cmake_progress_report /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/CMakeFiles $(CMAKE_PROGRESS_2)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building CXX object Sockets/CMakeFiles/Sockets.dir/ServerSocket.cpp.o"
	cd /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/Sockets && /usr/bin/c++   $(CXX_DEFINES) $(CXX_FLAGS) -o CMakeFiles/Sockets.dir/ServerSocket.cpp.o -c /home/rapha/Desktop/Cours/Reseaux/FerryInpres/Sockets/ServerSocket.cpp

Sockets/CMakeFiles/Sockets.dir/ServerSocket.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/Sockets.dir/ServerSocket.cpp.i"
	cd /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/Sockets && /usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -E /home/rapha/Desktop/Cours/Reseaux/FerryInpres/Sockets/ServerSocket.cpp > CMakeFiles/Sockets.dir/ServerSocket.cpp.i

Sockets/CMakeFiles/Sockets.dir/ServerSocket.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/Sockets.dir/ServerSocket.cpp.s"
	cd /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/Sockets && /usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -S /home/rapha/Desktop/Cours/Reseaux/FerryInpres/Sockets/ServerSocket.cpp -o CMakeFiles/Sockets.dir/ServerSocket.cpp.s

Sockets/CMakeFiles/Sockets.dir/ServerSocket.cpp.o.requires:
.PHONY : Sockets/CMakeFiles/Sockets.dir/ServerSocket.cpp.o.requires

Sockets/CMakeFiles/Sockets.dir/ServerSocket.cpp.o.provides: Sockets/CMakeFiles/Sockets.dir/ServerSocket.cpp.o.requires
	$(MAKE) -f Sockets/CMakeFiles/Sockets.dir/build.make Sockets/CMakeFiles/Sockets.dir/ServerSocket.cpp.o.provides.build
.PHONY : Sockets/CMakeFiles/Sockets.dir/ServerSocket.cpp.o.provides

Sockets/CMakeFiles/Sockets.dir/ServerSocket.cpp.o.provides.build: Sockets/CMakeFiles/Sockets.dir/ServerSocket.cpp.o

Sockets/CMakeFiles/Sockets.dir/Exception.cpp.o: Sockets/CMakeFiles/Sockets.dir/flags.make
Sockets/CMakeFiles/Sockets.dir/Exception.cpp.o: ../Sockets/Exception.cpp
	$(CMAKE_COMMAND) -E cmake_progress_report /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/CMakeFiles $(CMAKE_PROGRESS_3)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building CXX object Sockets/CMakeFiles/Sockets.dir/Exception.cpp.o"
	cd /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/Sockets && /usr/bin/c++   $(CXX_DEFINES) $(CXX_FLAGS) -o CMakeFiles/Sockets.dir/Exception.cpp.o -c /home/rapha/Desktop/Cours/Reseaux/FerryInpres/Sockets/Exception.cpp

Sockets/CMakeFiles/Sockets.dir/Exception.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/Sockets.dir/Exception.cpp.i"
	cd /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/Sockets && /usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -E /home/rapha/Desktop/Cours/Reseaux/FerryInpres/Sockets/Exception.cpp > CMakeFiles/Sockets.dir/Exception.cpp.i

Sockets/CMakeFiles/Sockets.dir/Exception.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/Sockets.dir/Exception.cpp.s"
	cd /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/Sockets && /usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -S /home/rapha/Desktop/Cours/Reseaux/FerryInpres/Sockets/Exception.cpp -o CMakeFiles/Sockets.dir/Exception.cpp.s

Sockets/CMakeFiles/Sockets.dir/Exception.cpp.o.requires:
.PHONY : Sockets/CMakeFiles/Sockets.dir/Exception.cpp.o.requires

Sockets/CMakeFiles/Sockets.dir/Exception.cpp.o.provides: Sockets/CMakeFiles/Sockets.dir/Exception.cpp.o.requires
	$(MAKE) -f Sockets/CMakeFiles/Sockets.dir/build.make Sockets/CMakeFiles/Sockets.dir/Exception.cpp.o.provides.build
.PHONY : Sockets/CMakeFiles/Sockets.dir/Exception.cpp.o.provides

Sockets/CMakeFiles/Sockets.dir/Exception.cpp.o.provides.build: Sockets/CMakeFiles/Sockets.dir/Exception.cpp.o

Sockets/CMakeFiles/Sockets.dir/SocketException.cpp.o: Sockets/CMakeFiles/Sockets.dir/flags.make
Sockets/CMakeFiles/Sockets.dir/SocketException.cpp.o: ../Sockets/SocketException.cpp
	$(CMAKE_COMMAND) -E cmake_progress_report /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/CMakeFiles $(CMAKE_PROGRESS_4)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building CXX object Sockets/CMakeFiles/Sockets.dir/SocketException.cpp.o"
	cd /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/Sockets && /usr/bin/c++   $(CXX_DEFINES) $(CXX_FLAGS) -o CMakeFiles/Sockets.dir/SocketException.cpp.o -c /home/rapha/Desktop/Cours/Reseaux/FerryInpres/Sockets/SocketException.cpp

Sockets/CMakeFiles/Sockets.dir/SocketException.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/Sockets.dir/SocketException.cpp.i"
	cd /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/Sockets && /usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -E /home/rapha/Desktop/Cours/Reseaux/FerryInpres/Sockets/SocketException.cpp > CMakeFiles/Sockets.dir/SocketException.cpp.i

Sockets/CMakeFiles/Sockets.dir/SocketException.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/Sockets.dir/SocketException.cpp.s"
	cd /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/Sockets && /usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -S /home/rapha/Desktop/Cours/Reseaux/FerryInpres/Sockets/SocketException.cpp -o CMakeFiles/Sockets.dir/SocketException.cpp.s

Sockets/CMakeFiles/Sockets.dir/SocketException.cpp.o.requires:
.PHONY : Sockets/CMakeFiles/Sockets.dir/SocketException.cpp.o.requires

Sockets/CMakeFiles/Sockets.dir/SocketException.cpp.o.provides: Sockets/CMakeFiles/Sockets.dir/SocketException.cpp.o.requires
	$(MAKE) -f Sockets/CMakeFiles/Sockets.dir/build.make Sockets/CMakeFiles/Sockets.dir/SocketException.cpp.o.provides.build
.PHONY : Sockets/CMakeFiles/Sockets.dir/SocketException.cpp.o.provides

Sockets/CMakeFiles/Sockets.dir/SocketException.cpp.o.provides.build: Sockets/CMakeFiles/Sockets.dir/SocketException.cpp.o

Sockets/CMakeFiles/Sockets.dir/SocketsUtils.cpp.o: Sockets/CMakeFiles/Sockets.dir/flags.make
Sockets/CMakeFiles/Sockets.dir/SocketsUtils.cpp.o: ../Sockets/SocketsUtils.cpp
	$(CMAKE_COMMAND) -E cmake_progress_report /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/CMakeFiles $(CMAKE_PROGRESS_5)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building CXX object Sockets/CMakeFiles/Sockets.dir/SocketsUtils.cpp.o"
	cd /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/Sockets && /usr/bin/c++   $(CXX_DEFINES) $(CXX_FLAGS) -o CMakeFiles/Sockets.dir/SocketsUtils.cpp.o -c /home/rapha/Desktop/Cours/Reseaux/FerryInpres/Sockets/SocketsUtils.cpp

Sockets/CMakeFiles/Sockets.dir/SocketsUtils.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/Sockets.dir/SocketsUtils.cpp.i"
	cd /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/Sockets && /usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -E /home/rapha/Desktop/Cours/Reseaux/FerryInpres/Sockets/SocketsUtils.cpp > CMakeFiles/Sockets.dir/SocketsUtils.cpp.i

Sockets/CMakeFiles/Sockets.dir/SocketsUtils.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/Sockets.dir/SocketsUtils.cpp.s"
	cd /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/Sockets && /usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -S /home/rapha/Desktop/Cours/Reseaux/FerryInpres/Sockets/SocketsUtils.cpp -o CMakeFiles/Sockets.dir/SocketsUtils.cpp.s

Sockets/CMakeFiles/Sockets.dir/SocketsUtils.cpp.o.requires:
.PHONY : Sockets/CMakeFiles/Sockets.dir/SocketsUtils.cpp.o.requires

Sockets/CMakeFiles/Sockets.dir/SocketsUtils.cpp.o.provides: Sockets/CMakeFiles/Sockets.dir/SocketsUtils.cpp.o.requires
	$(MAKE) -f Sockets/CMakeFiles/Sockets.dir/build.make Sockets/CMakeFiles/Sockets.dir/SocketsUtils.cpp.o.provides.build
.PHONY : Sockets/CMakeFiles/Sockets.dir/SocketsUtils.cpp.o.provides

Sockets/CMakeFiles/Sockets.dir/SocketsUtils.cpp.o.provides.build: Sockets/CMakeFiles/Sockets.dir/SocketsUtils.cpp.o

# Object files for target Sockets
Sockets_OBJECTS = \
"CMakeFiles/Sockets.dir/ClientSocket.cpp.o" \
"CMakeFiles/Sockets.dir/ServerSocket.cpp.o" \
"CMakeFiles/Sockets.dir/Exception.cpp.o" \
"CMakeFiles/Sockets.dir/SocketException.cpp.o" \
"CMakeFiles/Sockets.dir/SocketsUtils.cpp.o"

# External object files for target Sockets
Sockets_EXTERNAL_OBJECTS =

Sockets/libSockets.a: Sockets/CMakeFiles/Sockets.dir/ClientSocket.cpp.o
Sockets/libSockets.a: Sockets/CMakeFiles/Sockets.dir/ServerSocket.cpp.o
Sockets/libSockets.a: Sockets/CMakeFiles/Sockets.dir/Exception.cpp.o
Sockets/libSockets.a: Sockets/CMakeFiles/Sockets.dir/SocketException.cpp.o
Sockets/libSockets.a: Sockets/CMakeFiles/Sockets.dir/SocketsUtils.cpp.o
Sockets/libSockets.a: Sockets/CMakeFiles/Sockets.dir/build.make
Sockets/libSockets.a: Sockets/CMakeFiles/Sockets.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking CXX static library libSockets.a"
	cd /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/Sockets && $(CMAKE_COMMAND) -P CMakeFiles/Sockets.dir/cmake_clean_target.cmake
	cd /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/Sockets && $(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/Sockets.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
Sockets/CMakeFiles/Sockets.dir/build: Sockets/libSockets.a
.PHONY : Sockets/CMakeFiles/Sockets.dir/build

Sockets/CMakeFiles/Sockets.dir/requires: Sockets/CMakeFiles/Sockets.dir/ClientSocket.cpp.o.requires
Sockets/CMakeFiles/Sockets.dir/requires: Sockets/CMakeFiles/Sockets.dir/ServerSocket.cpp.o.requires
Sockets/CMakeFiles/Sockets.dir/requires: Sockets/CMakeFiles/Sockets.dir/Exception.cpp.o.requires
Sockets/CMakeFiles/Sockets.dir/requires: Sockets/CMakeFiles/Sockets.dir/SocketException.cpp.o.requires
Sockets/CMakeFiles/Sockets.dir/requires: Sockets/CMakeFiles/Sockets.dir/SocketsUtils.cpp.o.requires
.PHONY : Sockets/CMakeFiles/Sockets.dir/requires

Sockets/CMakeFiles/Sockets.dir/clean:
	cd /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/Sockets && $(CMAKE_COMMAND) -P CMakeFiles/Sockets.dir/cmake_clean.cmake
.PHONY : Sockets/CMakeFiles/Sockets.dir/clean

Sockets/CMakeFiles/Sockets.dir/depend:
	cd /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/rapha/Desktop/Cours/Reseaux/FerryInpres /home/rapha/Desktop/Cours/Reseaux/FerryInpres/Sockets /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/Sockets /home/rapha/Desktop/Cours/Reseaux/FerryInpres/build/Sockets/CMakeFiles/Sockets.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : Sockets/CMakeFiles/Sockets.dir/depend

