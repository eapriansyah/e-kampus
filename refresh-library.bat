@echo off
call yarn install
robocopy E:\apps\scm\repo\jhipster\generator-jhipster\generators .\node_modules\generator-jhipster\generators /MIR
call yarn webpack:build:vendor
