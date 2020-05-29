#-Xverify:none -Djava.nio.channels.spi.SelectorProvider=sun.nio.ch.EPollSelectorProvider
#-Xrunjdwp:transport=dt_socket,address=8888,suspend=n,server=y

java -Xverify:none -Xms64m -Xmx512m -XX:+HeapDumpOnOutOfMemoryError -Dtio.default.read.buffer.size=2048 -XX:HeapDumpPath=./jim-site-server-pid.hprof -cp ./:./lib/* org.jim.site.ImSiteServerStart