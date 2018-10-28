# Blog-Core

## Getting Started

## API Documentation

## Testing

## Notes
### How to configure image upload mode.
this blog has two types image upload mode.
you can configure the upload mode with system parameter.

#### Local Upload
upload the image to server local path.
this mode is default mode.

```bash
# Local Upload (default)
java -jar build/libs/blog-core-0.1.0.jar
# Local Upload
java -Dupload.type=LocalUpload -jar build/libs/blog-core-0.1.0.jar
```

#### S3 Upload
upload the image to Amazon S3.

```bash
# S3 Upload
java -Dupload.type=S3Upload -jar build/libs/blog-core-0.1.0.jar
```

## How to configure CORS
