# Github Repositories Lister
Github Repositories Lister is a Java web application which allows users to list all github repositories which are not forks.

## Table of Contents
- [Technologies](#technologies)
- [Installation](#installation)
- [Usage](#usage)
- [Responses](#responses)
- [License](#license)

## Technologies
Java 21
Spring Boot 3
Maven

## Installation
1. Clone the repository:
```bash
 git clone https://github.com/Julianklewek/GithubRepositoriesLister.git
```

2. Install dependencies and build project:
```mvn
mvn clean install
 ```
2. Run application
```mvn
mvn spring-boot:run
 ```

Or you can simply run application using Dockerfile.
```docker
docker build -t github-api .
docker run -d -p 8080:8080 github-api
```

## Usage
Application default port is 8080. Application has only one endpoint.

To list repositories with details for username:
```HTTP
GET /github/get-repos/{username}
Accept: application/json
```

## Responses

If successfully fetched repositories details for given username:
200 OK
```json
{
  "repositoryName":"${repositoryName}",
  "ownerLogin":"${ownerLogin}",
  "branches":[
    {
      "name":"${branchName}",
      "lastCommitSha":"${lastCommitSha}"
    }
  ]
}
```

If given username does not exists:
404 NOT_FOUND
```json
{
  "status":"${responseCode}",
  "message":"${whyHasItHappened}"
}
```

## License
Feel free to use.