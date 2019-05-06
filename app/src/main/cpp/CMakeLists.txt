#CMAKEList版本号
cmake_minimum_required(VERSION 3.4.1)
#Error while executing process /Users/xiaonian/Library/Android/sdk/cmake/3.10.2.4988404

#查找文件系统中指定模块的路径
file(GLOB bzip_source ${CMAKE_SOURCE_DIR}/bzip/*.c)

#设置本地的动态库，编译生成动态库
add_library(
        native-lib #模块名


        SHARED #动态库


        native-lib.cpp
        bspatch.c
        ${bzip_source}) #源文件

#查找系统库，比如：Log日志输出
find_library(
        log-lib


        log)

#需要链接或者编译库
target_link_libraries(
        native-lib

        ${log-lib})