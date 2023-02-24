# This bash script is for setting up the project in linux

sudo apt update

# install basic tools
sudo apt install zip
sudo apt install unzip
sudo apt install wget

# install git
sudo apt update
sudo apt install git

# install JDK 8
curl -s "https://get.sdkman.io" | bash
sdk install java 8.0.362-amzn
export JAVA_HOME=${dirname $(readlink -f $(which javac))}
export PATH=$JAVA_HOME/bin:$PATH

# install Apache Maven
wget https://dlcdn.apache.org/maven/maven-3/3.9.0/binaries/apache-maven-3.9.0-bin.tar.gz
tar -xvf apache-maven-3.9.0-bin.tar.gz
sudo mv apache-maven-3.9.0 /opt/
rm apache-maven-3.9.0-bin.tar.gz
export M2_HOME=/opt/apache-maven-3.9.0
export PATH=$M2_HOME/bin:$PATH

# install Apache Tomcat v9.0
wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.72/bin/apache-tomcat-9.0.72.tar.gz
tar -xvf apache-tomcat-9.0.72.tar.gz
rm apache-tomcat-9.0.72.tar.gz

# install mySQL
sudo wget https://dev.mysql.com/get/mysql-apt-config_0.8.24-1_all.deb
sudo dpkg -i mysql-apt-config_0.8.24-1_all.deb
sudo apt update
sudo apt-get install mysql-server mysql-client libmysqlclient-dev

# configure student-info-tracker project
# get the source code
git clone https://github.com/shiqiz1020/student-info-tracker-maven.git
# compile and create war
cd ./student-info-tracker-maven
mvn compile
mvn clean package
# move war to deploy in tomcat folder
cp target/student-info-tracker.war ~/apache-tomcat-9.0.72/webapps/
# stop tomcat
bash ~/apache-tomcat-9.0.72/bin/shutdown.sh
# start tomcat
bash ~/apache-tomcat-9.0.72/bin/startup.sh
