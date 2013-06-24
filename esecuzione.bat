java ^
-Djava.library.path="C:/Aparapi_2013_01_23_windows_x86_64" ^
 -Dcom.amd.aparapi.executionMode=%1 ^
 -classpath dist/ckjm.jar ^
 gr.spinellis.ckjm.MetricsFilter ^
 -c C:/ckjm/*.class
 Pause 