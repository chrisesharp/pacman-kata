#JAVA_FORMAT = pretty
export JAVA_FORMAT = progress

export TAG_FIXER = echo $(BDD)|sed "s/not /~/g" |sed "s/ or /,/g"

################################################################################
# Targets
################################################################################
BUILD_JAVA = mvn compile

TEST_JAVA = mvn test -Dcucumber.options="--glue com.example.pacman \
		--plugin $(JAVA_FORMAT) \
		--tags '($(BDD)) $(NOTRUN)' \
		classpath:features"

CLEAN_JAVA = mvn clean

#GENERATOR = swaggerapi/swagger-codegen-cli
GENERATOR = openapitools/openapi-generator-cli

DEPS_JAVA = docker run --rm -u $(UID) -v $(CURDIR):/local $(GENERATOR) generate \
		-i $(SCOREBOARD_API_YAML) \
		-g java \
		-o /local/target/generated-sources/openapi && \
		cd target/generated-sources/openapi && \
		mvn clean install

COVERAGE_JAVA = mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar \
		-Dsonar.host.url=$(SONAR_URL) \
		-Dsonar.organization=$(SONAR_ORG) \
		-Dsonar.projectKey=org.$(SONAR_ORG).pacman-kata-java \
		-Dsonar.projectName=pacman-kata-java \
		-Dsonar.exclusions="**/*.xml,**/swagger/**,**/openapi/**" \
    -Dsonar.login=$(SONAR_TOKEN) && \
	mvn com.gavinmogan:codacy-maven-plugin:coverage \
		-DcoverageReportFile=target/site/jacoco-ut/jacoco.xml \
		-DprojectToken=$(CODACY_PROJECT_TOKEN) -DapiToken=$(CODACY_API_TOKEN)

DEPLOY_JAVA = mvn install -Dmaven.test.skip=true