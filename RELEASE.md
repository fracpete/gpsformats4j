How to make a release
=====================

* Run the following command to deploy the artifact:

  ```
  mvn release:clean release:prepare release:perform
  ```

* push all changes

* go to the following URL and publish the artifact:

  ```
  https://central.sonatype.com/publishing/deployments
  ```

* update release tag on github (`vX.Y.X`, with `X.Y.Z` taken from 
  `pom.xml`), add some release notes and upload the `-bin.zip`

