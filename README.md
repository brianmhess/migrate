##Commands


###Build

Build your container with:
```
gcloud builds submit --tag gcr.io/PROJECT-ID/migrate
```

###Deploy

Deploy using
``` 
gcloud beta run deploy --image gcr.io/PROJECT-ID/migrate --platform managed
```
- You will be prompted for the service name: press Enter to accept the default name, `migrate`.
- You will be prompted for region: select the region of your choice, for example `us-central1`.
- You will be prompted to allow unauthenticated invocations: respond `y` .

###Removing

- In the GCP Console, go to the Manage resources page.
- In the project list, select the project you want to delete and click Delete delete.
- In the dialog, type the project ID, and then click Shut down to delete the project.
