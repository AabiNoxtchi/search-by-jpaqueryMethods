# Getting Started  

### The Application ###  

This system aims to demonstrate the use of JPA query methods  
- Pros of This Approach:  
  - No reliance on Specifications or Criteria API, or third party libraries.  
  - Simpler and more intuitive for small-scale filtering needs.  
- Cons:  
  - Less flexible than a Specification or Criteria API for complex filtering.  
  - and specific for this use case and the dynamic filtering approach: 
    - Potential performance issues if the dataset is large, as filtering is done in memory (retainAll).
    - and multiple select queries are done to database
- result:
  - for more complex or dynamic queries as this system's dynamic approach, Specifications or criteria Api, or Querydsl is more suitable choice
<br>

### JPA Query Methods: A Comprehensive Overview ###  

JPA query methods are a feature of Spring Data JPA that allows developers to create custom queries by defining methods in repository interfaces.
These methods follow naming conventions to dynamically generate database queries at runtime.  

- Key Features  
  - Convention-Based Querying: Queries are derived from method names based on property names and keywords.
  - Supports Common SQL Operations: SELECT, WHERE, JOIN, ORDER BY, and more.
  - Custom Queries: Use JPQL (@Query) or native SQL when conventions are insufficient.  
<br>
- Naming Conventions  
    The method name must follow this structure:
    
    `findBy | readBy | queryBy | getBy [Property][Operator][Condition]`  
<br>
- Common Keywords: `findBy`, `countBy`, `deleteBy`, `existsBy`  
  Examples: `findByName(String name)`, `countByEmail(String email)`, `deleteById(Long id)`  
<br>
- Query Conditions:  
  - `Is, Equals`: `findByName(String name)`
  - `Between`: `findByAgeBetween(int start, int end)`
  - `Like`: `findByNameLike(String name)`
  - `Contains`: `findByNameContains(String name)`
  - `In`: `findByAgeIn(List<Integer> ages)`
  - and more  
<br>
- Sorting and Pagination  
  - Sorting
    Add a Sort parameter to sort results dynamically:  
        `List<Student> findByName(String name, Sort sort);`  
    <br>
    Usage:
    ```
        Sort sort = Sort.by(Sort.Direction.ASC, "age");
        repository.findByName("John", sort);
    ```

  - Pagination
    Use a Pageable parameter for paginated results:  
    `Page<Student> findByAgeGreaterThan(int age, Pageable pageable);`  
    <br>
    Usage:
    ```
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        repository.findByAgeGreaterThan(18, pageable);
    ```  

- Combining Criteria
    And, Or, Not are used to combine conditions:
    ```
        List<Student> findByNameAndAge(String name, int age);
        List<Student> findByNameOrEmail(String name, String email);
    ```  

- Custom Queries with @Query
    When method names become too complex or non-standard operations are needed, @Query is used for JPQL or native SQL.

    - JPQL
    ```
        @Query("SELECT s FROM Student s WHERE s.name = :name AND s.age > :age")
        List<Student> findCustomQuery(@Param("name") String name, @Param("age") int age);
    ```  

    - Native Query
    ```
        @Query(value = "SELECT * FROM student WHERE email LIKE %:email%", nativeQuery = true)
        List<Student> findByEmailNative(@Param("email") String email);
    ```  

- Additional Features
  - Projections: to Fetch specific fields or DTOs instead of full entities.
    ```
    interface NameProjection {
    String getName();
    }
    
    List<NameProjection> findByAge(int age);
    ```  

  - Stream Results: Usage Java Streams for large result sets.
    ```
    @Query("SELECT s FROM Student s WHERE s.age > :age")
    Stream<Student> streamAllByAge(@Param("age") int age);
    ```  
    
  - Entity Graphs: @EntityGraph is used for optimizing queries by specifying relationships to fetch eagerly.
    ```
    @EntityGraph(attributePaths = {"enrollments", "enrollments.course"})
    List<Student> findAllWithEnrollments();
    ```  
    <br>

- Best Practices  
  - Keep Method Names Descriptive but Concise: Avoid overly complex names by splitting into smaller methods or using @Query.
  - Avoid Fetching Large Collections: Use pagination or streaming for large datasets.
  - Use Entity Graphs for Performance: Prevent the N+1 query problem by pre-fetching related entities.
  - Fallback to @Query When Necessary: Use custom JPQL or SQL for advanced queries. 
  - for more complex or dynamic queries as this system's dynamic approach, Specifications or criteria Api, or Querydsl is more suitable choice
<br><br>

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.5/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.5/gradle-plugin/packaging-oci-image.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.3.5/reference/data/sql.html#data.sql.jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

