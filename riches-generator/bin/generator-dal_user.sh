#!/bin/sh

mvn -Dmybatis.generator.configurationFile=src/main/resources/dal/generator-config_user.xml mybatis-generator:generate