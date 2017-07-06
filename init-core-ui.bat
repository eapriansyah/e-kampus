@echo off
robocopy E:\apps\templates\core-ui . /MIR
robocopy E:\apps\scm\repo\jhipster\generator-jhipster\generators .\node_modules\generator-jhipster\generators /MIR
copy E:\apps\scm\repo\jhipster\generator-jhipster\package.json .\node_modules\generator-jhipster /Y
call yo jhipster
robocopy E:\apps\scm\repo\jhipster\generator-jhipster\generators .\node_modules\generator-jhipster\generators /MIR
copy E:\apps\scm\repo\jhipster\generator-jhipster\package.json .\node_modules\generator-jhipster /Y