JDBC can be nice
=======================

This 'toy' library is not intended for large scale projects, but rather for quick hacking with jdbc, making it less painful and more pleasant to use.

Building
--------

You need a Java 5 (or newer) environment and Maven 2.0.9 (or newer) installed:

    $ mvn --version
    Apache Maven 3.0-alpha-5 (r883378; 2009-11-23 16:53:41+0100)
    Java version: 1.6.0_15
    Java home: /usr/lib/jvm/java-6-sun-1.6.0.15/jre
    Default locale: en_US, platform encoding: UTF-8
    OS name: "linux" version: "2.6.31-12-generic" arch: "i386" Family: "unix"

You should now be able to do a full build of `jdbc-can-be-nice`:

    $ git clone git://github.com/jawher/jdbc-can-be-nice.git
    $ cd jdbc-can-be-nice
    $ mvn clean install

To use this library in your projects, add the following to the `dependencies` section of your
`pom.xml`:

    <dependency>
      <groupId>org.nothing</groupId>
      <artifactId>jdbc-can-be-nice</artifactId>
      <version>0.9-SNAPSHOT</version>
    </dependency>

If you don't use Maven, take `jdbc-can-be-nice-0.9-SNAPSHOT.jar` and all of its dependencies, and add them to your classpath.


Troubleshooting
---------------

Please consider using [Github issues tracker](http://github.com/jawher/jdbc-can-be-nice/issues) to submit bug reports or feature requests.


Using this library
------------------

Here is a sample showing the usage of many features of this library, like transactions, chaining, querying and generated keys retrieval :

    ConnectionProvider connectionProvider = cachingConnectionProvider(driverManagerConnectionProvider(
		"org.apache.derby.jdbc.Driver40",
		"jdbc:derby:crud;create=true", "", ""));
    Number key = doWithConnection(
		sqlTx(
				sqlUpdate("delete * from table").then(
						sqlUpdate("insert into table values(?, ?)",
								true, "a"))).thenReturn(
				dontThrowSqlException(sqlUpdateAndReturnKey(
						"insert into table2 values(?)", 8), -1)),
		connectionProvider);
    RowMapper<String> namesMapper = new RowMapper<String>() {

	public String mapRow(ResultSet resultSet, int row)
			throws SQLException {
		return resultSet.getString("name");
	}
    };

    List<String> names = doWithConnection(sqlQuery(
		"select name from table where age < ?", namesMapper, 20),
		connectionProvider);

License
-------

See `LICENSE` for details.
