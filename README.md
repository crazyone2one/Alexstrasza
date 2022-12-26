# Alexstrasza
>Life is my Aspect, Dark One, and I, like all mothers, know both the pain and wonders that entails! For the past several years, I have watched my children be raised as instruments of war, slaughtered if they proved insufficient or too willful! I have lived knowing that so many died that I could do nothing for!
## Project setup
~~~
alexstrasza
├─┬ backend     → backend module with Spring Boot code
│ ├── src
│ └── pom.xml
├─┬ frontend    → frontend module with Vue.js code
│ ├── src
│ └── pom.xml
└── pom.xml     → Maven parent pom managing both modules
~~~
## First App run
~~~
mvn clean install
~~~
~~~
mvn --projects backend spring-boot:run
~~~