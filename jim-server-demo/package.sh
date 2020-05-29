#!/bin/bash
echo =================================
echo J-IM自动化打包脚本启动...
echo =================================
curdir=${PWD##*/}
echo 开始构建文件...
rm -rf build
mvn clean package -Dmaven.test.skip=true
mvn dependency:copy-dependencies -DoutputDirectory=build/$curdir/lib
echo 构建文件完成...
echo 开始打包...
cp  target/*.jar build/$curdir/lib
cp -r target/classes/config build/$curdir/config
cp -r target/classes/pages build/$curdir/pages
cp startup.sh build/$curdir/
rm -rf target
echo 打包完成...
echo =================================
echo J-IM自动打包完成,打包目录位置在当前build目录下:${PWD}/build
echo =================================