# Book Library

I've spent most of 2018 working with kafka and kafka streams during my day job.
This project is my playground for seeing what's possible with ktables.

I use kafka as the source of truth for all data,
 but I use kafka connect to present a searchable view of the dat using elastic.
 
 To setup the development environment, just execute docker-compose up.
 This will start up kafka, elastic, and connect.
 When kafka-connect comes online POST the following to localhost:8083/connectors to start the kafka to elastic connecter
 
 ```$json
{
	"name": "elasticsearch-sink", 
	"config": {
		"connector.class": "io.confluent.connect.elasticsearch.ElasticsearchSinkConnector",
		"tasks.max" : "1",
		"topics": "MEMBER.VIEW.ELASTIC",
		"connection.url": "http://elastic:9200",
		"type.name": "_doc",
		"topic.index.map":"MEMBER.VIEW.ELASTIC:member",
		"schema.ignore": "true"
	}
}
```
