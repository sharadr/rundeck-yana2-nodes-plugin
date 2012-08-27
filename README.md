rundeck-yana2-nodes-plugin
==========================

Provides Node Information from Yana2 to Rundeck

Building
------------

Setting up:

* Download Apache Maven 3.0 
* Unzip and add bin folder to PATH
* Execute the following to install rundeck core jar (which you can find in your Rundeck installation, or here: http://build.rundeck.org/job/rundeck-development) locally:

    mvn install:install-file -DgroupId=org.rundeck.core -DartifactId=rundeck-core -Dversion=1.4.3 -Dpackaging=jar -DgeneratePom=true -Dfile=rundeck-core-1.4.3.jar

* Run mvn clean install to build.

Note that the plug-in is also being continuously built at http://services.dtosolutions.com:8080/job/rundeck-yana2-nodes-plugin/

Plug-in Installation
------------

Just drop the build (e.g. rundeck-yana2-nodes-plugin-1.1.jar from the project's Downloads section) into ${rdeck.base}/libext (creating the directory, if necessary) and use the Rundeck project setup UI to configure Yana2 resources as the resource model source.

Setting up Yana2
------------

Here's the minimum Yana XML to load through the Yana2 UI to get your first node:

<!-- Sample XML to establish the minimum set of node types to support default usage of the      -->
<!-- Rundeck rundeck-yana2-nodes-plugin (https://github.com/sharadr/rundeck-yana2-nodes-plugin) -->

    
    <yana>
      <attributes>
        <attribute name="hostname" filter="String"/>
        <attribute name="osArch" filter="String"/>
        <attribute name="osFamily" filter="String"/>
        <attribute name="osName" filter="String"/>
        <attribute name="osVersion" filter="String"/>
        <attribute name="username" filter="String"/>
      </attributes>

      <types>
        <type name="node">
          <description>Rundeck node (system/server) type</description>
          <image>Node.png</image>
          <attributes>
            <attribute name="hostname" required="true"/>
            <attribute name="osArch" required="false"/>
            <attribute name="osFamily" required="false"/>    
            <attribute name="osName"  required="false"/>
            <attribute name="osVersion" required="false"/>
            <attribute name="username" required="false"/>
          </attributes>
        </type>
      </types>

      <nodes>
        <node name="centos62-rundeck-tomcat" type="node" tags="tag1,tag2,tag3">
          <description>Sample node instance</description>
          <attributes>
            <attribute name="hostname" value="centos62-rundeck-tomcat"/>
            <attribute name="osArch" value="amd64"/>
            <attribute name="osFamily" value="unix"/>
            <attribute name="osName" value="Linux"/>
            <attribute name="osVersion" value="2.6.32-220.el6.x86_64"/>
            <attribute name="username" value="tomcat"/>
          </attributes>
        </node>
      </nodes>
    </yana>