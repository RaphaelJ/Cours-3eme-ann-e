# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 2.8

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
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
CMAKE_SOURCE_DIR = /home/rapha/Cours/Reseaux/FerryInpres

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/rapha/Cours/Reseaux/FerryInpres

# Include any dependencies generated for this target.
include IniParser/CMakeFiles/IniParser.dir/depend.make

# Include the progress variables for this target.
include IniParser/CMakeFiles/IniParser.dir/progress.make

# Include the compile flags for this target's objects.
include IniParser/CMakeFiles/IniParser.dir/flags.make

IniParser/CMakeFiles/IniParser.dir/IniParser.cpp.o: IniParser/CMakeFiles/IniParser.dir/flags.make
IniParser/CMakeFiles/IniParser.dir/IniParser.cpp.o: IniParser/IniParser.cpp
	$(CMAKE_COMMAND) -E cmake_progress_report /home/rapha/Cours/Reseaux/FerryInpres/CMakeFiles $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building CXX object IniParser/CMakeFiles/IniParser.dir/IniParser.cpp.o"
	cd /home/rapha/Cours/Reseaux/FerryInpres/IniParser && /usr/bin/c++   $(CXX_DEFINES) $(CXX_FLAGS) -o CMakeFiles/IniParser.dir/IniParser.cpp.o -c /home/rapha/Cours/Reseaux/FerryInpres/IniParser/IniParser.cpp

IniParser/CMakeFiles/IniParser.dir/IniParser.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/IniParser.dir/IniParser.cpp.i"
	cd /home/rapha/Cours/Reseaux/FerryInpres/IniParser && /usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -E /home/rapha/Cours/Reseaux/FerryInpres/IniParser/IniParser.cpp > CMakeFiles/IniParser.dir/IniParser.cpp.i

IniParser/CMakeFiles/IniParser.dir/IniParser.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/IniParser.dir/IniParser.cpp.s"
	cd /home/rapha/Cours/Reseaux/FerryInpres/IniParser && /usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -S /home/rapha/Cours/Reseaux/FerryInpres/IniParser/IniParser.cpp -o CMakeFiles/IniParser.dir/IniParser.cpp.s

IniParser/CMakeFiles/IniParser.dir/IniParser.cpp.o.requires:
.PHONY : IniParser/CMakeFiles/IniParser.dir/IniParser.cpp.o.requires

IniParser/CMakeFiles/IniParser.dir/IniParser.cpp.o.provides: IniParser/CMakeFiles/IniParser.dir/IniParser.cpp.o.requires
	$(MAKE) -f IniParser/CMakeFiles/IniParser.dir/build.make IniParser/CMakeFiles/IniParser.dir/IniParser.cpp.o.provides.build
.PHONY : IniParser/CMakeFiles/IniParser.dir/IniParser.cpp.o.provides

IniParser/CMakeFiles/IniParser.dir/IniParser.cpp.o.provides.build: IniParser/CMakeFiles/IniParser.dir/IniParser.cpp.o

# Object files for target IniParser
IniParser_OBJECTS = \
"CMakeFiles/IniParser.dir/IniParser.cpp.o"

# External object files for target IniParser
IniParser_EXTERNAL_OBJECTS =

IniParser/libIniParser.a: IniParser/CMakeFiles/IniParser.dir/IniParser.cpp.o
IniParser/libIniParser.a: IniParser/CMakeFiles/IniParser.dir/build.make
IniParser/libIniParser.a: IniParser/CMakeFiles/IniParser.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking CXX static library libIniParser.a"
	cd /home/rapha/Cours/Reseaux/FerryInpres/IniParser && $(CMAKE_COMMAND) -P CMakeFiles/IniParser.dir/cmake_clean_target.cmake
	cd /home/rapha/Cours/Reseaux/FerryInpres/IniParser && $(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/IniParser.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
IniParser/CMakeFiles/IniParser.dir/build: IniParser/libIniParser.a
.PHONY : IniParser/CMakeFiles/IniParser.dir/build

IniParser/CMakeFiles/IniParser.dir/requires: IniParser/CMakeFiles/IniParser.dir/IniParser.cpp.o.requires
.PHONY : IniParser/CMakeFiles/IniParser.dir/requires

IniParser/CMakeFiles/IniParser.dir/clean:
	cd /home/rapha/Cours/Reseaux/FerryInpres/IniParser && $(CMAKE_COMMAND) -P CMakeFiles/IniParser.dir/cmake_clean.cmake
.PHONY : IniParser/CMakeFiles/IniParser.dir/clean

IniParser/CMakeFiles/IniParser.dir/depend:
	cd /home/rapha/Cours/Reseaux/FerryInpres && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/rapha/Cours/Reseaux/FerryInpres /home/rapha/Cours/Reseaux/FerryInpres/IniParser /home/rapha/Cours/Reseaux/FerryInpres /home/rapha/Cours/Reseaux/FerryInpres/IniParser /home/rapha/Cours/Reseaux/FerryInpres/IniParser/CMakeFiles/IniParser.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : IniParser/CMakeFiles/IniParser.dir/depend
