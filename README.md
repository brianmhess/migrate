## Commands

### Setup
```
gcloud beta init
gcloud config set project PROJECT-ID
gcloud config set run/region us-central1
```

### Build

Build your container with:
```
gcloud builds submit --tag gcr.io/PROJECT-ID/migrate
```

### Deploy

Deploy using
```
gcloud beta run deploy SERVICE-NAME --image gcr.io/PROJECT-ID/migrate --allow-unauthenticated --platform managed --project=PROJECT-ID
```

### Removing

- In the GCP Console, go to the Manage resources page.
- In the project list, select the project you want to delete and click Delete delete.
