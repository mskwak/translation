#!/bin/sh

BASEDIR=/opt/postgresql
DATADIR=$BASEDIR/data
DBUSER=hangugi

export BASEDIR DATADIR DBUSER

start() {
	su - $DBUSER -c "$BASEDIR/bin/pg_ctl start -D $DATADIR &"
}

stop() {
	su - $DBUSER -c "$BASEDIR/bin/pg_ctl stop -w -D $DATADIR -m fast"
}

reload() {
	su - $DBUSER -c "$BASEDIR/bin/pg_ctl reload -D $DATADIR"
}

case "$1" in
	start)
		start
		;;
	stop)
		stop
		;;
	reload)
		reload
		;;
	*)
		echo "$0 {start|stop|reload}"	
esac
