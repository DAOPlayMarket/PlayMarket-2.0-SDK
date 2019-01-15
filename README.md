# PlayMarket 2.0 SDK
![PlayMarket 2.0](https://github.com/CryptonStudio/PlayMarket-2.0-SDK/blob/master/pm2_logo.png)

DAO PlayMarket 2.0 is a decentralized Android App Store that accepts payments in cryptocurrency and is combined with an ICO platform for developers.


Personal freedom is impossible without economic security and independence. Therefore, we created an open, censorship-resistant marketplace using blockchain and smart contracts.

## Installation
Add this library to your project.

## Usage
Use the PlayMarket class to bring up the purchase screen.

#### setAppName
Use this method to set the name of your application.

#### setDescription
Use this method to set your payment description.

#### setObjectId
Use this method to set a unique identifier of purchased object.

#### setTransactionType
Use one of transaction types:
1. TRANSACTION_BUY - Transaction without checks;
2. TRANSACTION_BUY_OBJECT - Buy object without check price;
3. TRANSACTION_BUY_OBJECT_WITH_PRICE_CHECK - Buy object with check price;
4. TRANSACTION_BUY_SUB - Buy subscription without check price;
5. TRANSACTION_BUY_SUB_WITH_PRICE_CHECK - Buy subscription with check price;

To receive the result override onActivityResult method. 

## Example
```java
new PlayMarket().setAppName("App name")
                        .setDescription("Payment description")
                        .setOjectId("10")
                        .setPriceInUnit("1")
                        .setTransactionType(PlaymarketConstants.TRANSACTION_BUY_OBJECT)
                        .build(Context or Activity with RESULT_CODE);
```
                        
```java                                           
@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String resultUrl = intent.getStringExtra(PlaymarketConstants.TRANSACTION_RESULT_URL);
            String transactionHash = intent.getStringExtra(PlaymarketConstants.TRANSACTION_RESULT_TXHASH);
        }
    }
```
Where:

resultUrl - link to etherscan.io. (https://etherscan.io/tx/0x00d021f35da36e30f3c269f1e10266e978832df4d67yc3bf6ac3410d508e77fc)

transactionHash - hash of generated transaction.


                        
 
                     
