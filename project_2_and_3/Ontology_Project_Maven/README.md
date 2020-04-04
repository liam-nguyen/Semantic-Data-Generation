<br />
<p align="center">
  <h3 align="center">Semantic Data Generation</h3>

  <p align="center">
    Build Ontology Graph Knowledge from Public Datasets
    <br />
    <a href="https://github.com/othneildrew/Best-README-Template"><strong>Explore the docs »</strong></a>
    <br />
    <br />
  </p>
</p>

<!-- ABOUT THE PROJECT -->

## About The Project

Visit data.gov to select 2-3 datasets of interest and write a program to convert these
datasets into semantic data in .rdf or .owl format

## Datasets

- [Hospital General Information](https://data.medicare.gov/Hospital-Compare/Hospital-General-Information/xubh-q36u)
- [Medicare Hospital Spending by Claim 2018](https://data.medicare.gov/Hospital-Compare/Medicare-Hospital-Spending-by-Claim/nrth-mfg3)
- [Timely and Effective Care](https://healthdata.gov/dataset/timely-and-effective-care-hospital)

## Technologies

- [Apache Jena](https://jena.apache.org/)
- [Apache Maven](https://maven.apache.org/)
- [Apache Fuseki](https://jena.apache.org/documentation/fuseki2/)

<!-- GETTING STARTED -->

## Getting Started

### Prerequisites

- [Apache Maven 3.6.3](https://maven.apache.org/download.cgi)
- [JDK 8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)

## Usage
### Project 2
- Everything can be found in **deliverables**
- To execute the fat jar:

```sh
java -jar [jar_name].jar
```
- After the execution, it will produce Hospital.owl within the **deliverables** directory. 
### Project 3
- To use SPARQL query with Hospital.owl ontology, run:

```shell script
java -jar apache-jena-fuseki-3.14.0/fuseki-server.jar
```
- It will start a server, navigate to: **localhost:3030**
- Go to manage **dataset**
- Create a new **dataset** and upload the owl file
- Run query at **dataset**