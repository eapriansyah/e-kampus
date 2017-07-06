@echo off
cls
call mvn spring-boot:run -Dmaven.test.skip=true -Drun.jvmArguments="-agentpath:d:/apps/tools/jrebel/lib/jrebel64.dll"