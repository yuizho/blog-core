# Blog-Core
[![CircleCI](https://circleci.com/gh/yuizho/blog-core/tree/master.svg?style=shield)](https://circleci.com/gh/yuizho/blog-core/tree/master)
[![codecov](https://codecov.io/gh/yuizho/blog-core/branch/master/graph/badge.svg)](https://codecov.io/gh/yuizho/blog-core)

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

configure S3 information as environment variable

```bash
export S3_ACCESS_KEY="<your access key>"
export S3_SECRET_KEY="<your secret key>"
export S3_END_POINT="<your S3's endpoint>"
export S3_REGION="<your S3's region name>"
export S3_BUCKET_NAME="<your S3's bucket name>"
```

then you can launch blog-core server as S3Upload mode

```bash
# S3 Upload
java -Dupload.type=S3Upload -jar build/libs/blog-core-0.1.0.jar
```

## How to configure CORS
