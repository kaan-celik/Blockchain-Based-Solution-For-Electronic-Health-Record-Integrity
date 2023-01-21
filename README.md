# Blockchain Based Solution For Electronic Health Record Integrity

With the development of technology, great developments have occurred in the healthcare area,
Specially, many solutions have been proposed for the processing of electronic health data.
And some of them have been developed. As a fact, there are critical factors that should be considered
under these developments. The electronic health information are demanded by some harmful organizations and people.
In addition, there is an extensive market for these informations. Therefore, in electronic health data systems,
the privacy of the patient, the executability of the system, and its protection against attacks are milestone requirements.
One of the methods offered to provide these security measures is the blockchain technology.
Therefore, in this project, a blockchain based medical survey system is proposed to achieve the data integrity.
The system is implemented on the Algorand blockchain.


## Installation & Setup

### Prerequesities
* PostgreSQL
* Java 11 +
* Java IDE (**IntelliJ recommended**)
* Git Bash (Not necessary)
* npm

After the download project and satisfy the prerequesities, the installation steps given below:

* Run the **script.sql** for the database generation
* Specify the database and server settings with the **application.yml** (src/main/resources/application.yml)
* You need to API key for the using Algorand API. Therefore you should obtain an API key from [Purestake](https://developer.purestake.io/)
* After you get the key, you must paste it where it says "/*Enter your purestake.io token*/" in the project files.
* You are ready to execute the project with the given server settings.
* After that, you should modify the **../frontend/webpack.config.js** to your **application.yml** for the frontend.
* Then, with the command prompt of your IDE or Git Bash, you can start the frontend as
```bash 
  cd ../src/main/frontend
  npm install
  npm start
```

## Scenario
 1. The admin / authorized user creates a survey
 2. The users / patients try to read and approve the consent form
 3. If the user / patient has access for the created survey then it can fill the survey
 4. Finally, the filled survey has hashed and it is saved to the Algorand blockchain as a proof. 




## Notes
> **Warning**
> You should always use Algorand Testnet for the blockchain operations and wallet/account creations

* For the Algorand test user you can use the **AlgoDEA** plugin for the IntelliJ IDEA. You can do every action in the Algorand testnet.
* If you don't use IntelliJ you can download the Algorand Wallet in your mobile phone and can create an account with **!!Algorand Testnet!!**.
* You can track any activity of your Account with [Algorand Testnet](https://testnet.algoexplorer.io/).
* You can fund your account with [Algorand Dispenser](https://bank.testnet.algorand.network/).


## API Usage

#### Retrieve all surveys

```http
  GET /surveys
```

#### Retrieve selected survey by application Id

```http
  GET /surveys/${appId}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `appId`      | `string` | **Required**. Survey application id key for Algorand |

#### Create a new survey

```http
  POST /survey/create
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `surveyRequest`      | `body` | **Required**. Survey object JSON|


#### Consent and registiration for the selected survey
```http
  POST /consent/survey/${appId}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `appId`      | `string` | **Required**. Survey application id key for Algorand |
| `mnemonicKey`      | `body` | **Required**. Mnemonic key for Algorand Authorization |

#### Fill selected survey
```http
  POST /fill/survey/${appId}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `appId`      | `string` | **Required**. Survey application id key for Algorand |
| `fillRequest`      | `body` | **Required**. Filled Survey object JSON |



## Questions & Feedbacks

If you have any question or feedback, Please create an issue or contact me.

  