# Set the UMASK so the log files are readable in the Tomcat dashboard.
umask 007

export $JAVA_APP_ENV=cert

INSTALL_DIR=/appl/services/PublishWICItemsToProductMaster_J50X100

# Configure Java
JAVA=/appl/jdk/jre/bin/java

# The jar file the application lives in
JAR=$INSTALL_DIR/PublishWICItemsToProductMaster_J50X100D-1.0.0.jar

# Active profiles
ACTIVE_PROFILES=$JAVA_APP_ENV

# The file with the job configuration
JOB_FILE=jobs.xml

# The name of the job to run
JOB_NAME=J50X100D

# Any paramaters to pass to the job
JOB_PARMS=currentDate=\'$(date +'%m/%d/%YT%H:%M:%S')\'

# Execute the job
$JAVA -Dspring.profiles.active=$ACTIVE_PROFILES -jar $JAR $JOB_FILE $JOB_NAME $JOB_PARMS

# Whatever you want to be the return value of this script to be what you want the scheduler to
# know about, basically the success or failure of your job. In most cases, the above line that
# executes the job should be your last line in the script.
exit $?