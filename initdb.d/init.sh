#!/bin/bash

mysql=( mysql --protocol=socket -uroot -p"${MYSQL_ROOT_PASSWORD}" )

"${mysql[@]}" <<-EOSQL
    grant all privileges on test.* to '${MYSQL_USER}'@'%' ;
EOSQL