# Overview
This is a pet project in which I tried to implement a container that is similiar to Spring Boot assembeles project. 
To use you need to type API mappings in pseudo http requests manner that is descriped is usage section. Before start application 
assembles bean map and creates global context that can be used for injections when using annotations like ```@Inject``` or ```@DataAccessObject```

# Build
<p>To run and build a project you can</p>
<pre><code>$ mvn exec:java -Dexec.mainClass=org.vsu.rudakov.Main</code></pre>
<p>To be able to use PostgreSQL database instead of file database run Docker script</p>
<pre><code>$ docker-compose up</code></pre>

# Usage
<p>To make a request in your terminal you can type commands like these</p>
<p>To get all child entities in database</>
<pre><code>$ child -get</code></pre> 
<p>To get child entity with id 1</>
<pre><code>$ child/1 -get</code></pre> 
<p>To update child entity with id 1 with data passed in payload</>
<pre><code>$ child -update [child={id: 1, firstName: peter, lastName: pupkin, groupId: 1}]</code></pre> 
<p>To save new entity with data passed in payload</>
<pre><code>$ child -post [child={id: 3, firstName: петр, lastName: пупкин, groupNumber: 2}]</code></pre> 
<p>To delete child with id 1</>
<pre><code>$ child/1 -delete</code></pre> 

To make changes to configuration edit ```src/main/resources/properties.conf``` file
