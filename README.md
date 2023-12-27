<img height="200" src="book_store_image.png" width="500" style="display: block; margin: 0 auto; margin-top: 1pc;">
<h1 style="text-align: center; font-size: 36px; margin-top: -0.5pc;">Book store</h1>
<h1 style="font-size: 24.5px; margin-top: -2pc; margin-left: 0pc;">Introduction</h1>
<p style="font-size: 16px; margin-top: -0.8pc;">Book Store is an online platform created for user convenience in searching, browsing, and purchasing books through the Internet. It solves several issues: 😊📚</p>
<ul style="font-size: 16px; padding-left: 20px; margin-top: -0.8pc; margin-left: 0pc;">
    <li>Convenient book search: Users can search for books by title and author, allowing them to quickly find the desired books 🔍📚.</li>
    <li>Book purchasing: Users have the option to purchase books directly through this platform, conveniently selecting desired books and putting them into a shopping cart 🛒📚.</li>
</ul>

<h1 style="font-size: 24.5px; margin-top: -1pc; margin-left: 0pc;">Technology stack</h1>
<p style="font-size: 16px; margin-top: -0.8pc;">While working on the project, I used the following technologies for the following purposes: 😊🛠️</p>
<ul style="font-size: 16px; padding-left: 20px; margin-top: -0.8pc; margin-left: 0pc;">
    <li>Accessing Data: Spring Data JPA, Hibernate, MySQL 🗃️</li>
    <li>Web Development: Spring MVC, Servlets, JSP, Tomcat 🌐</li>
    <li>Application Configuration: Spring Boot, Spring, Maven ⚙️</li>
    <li>Testing and Documentation: JUnit, Mockito, Swagger, Test Containers 🧪📝</li>
    <li>Programming Language: Java ☕</li>
    <li>Version Control: Git 🔄</li>
    <li>Developer Tools: IDE 💻</li>
    <li>Data Formats: XML, JSON 📄</li>
    <li>Security: Spring Security 🔒</li>
</ul>

<h1 style="font-size: 24.5px; margin-top: -1pc; margin-left: 0pc;">Functionality of controllers</h1>
<p style="font-size: 20px; margin-top: -1pc;">Authentication Controller</p>
<p style="margin-top: -1pc;"><code>/api/auth/registration  // POST</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;"><a href="https://www.postman.com/lunar-module-cosmologist-43034160/workspace/book-store/request/31108999-c94028e2-c585-4926-9e4a-34cab921f43b">This</a> endpoint is intended for user registration. Example of request body:</p>
<pre style="margin-top: -0.6pc;">
  <code>
{
  "email": "john.doe@example.com",
  "password": "securePassword123",
  "repeatPassword": "securePassword123",
  "firstName": "John",
  "lastName": "Doe",
  "shippingAddress": "123 Main St, City, Country"
}
  </code>
</pre>
<p style="margin-top: 1.5pc;"><code>/api/auth/login  // POST</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;"><a href="https://www.postman.com/lunar-module-cosmologist-43034160/workspace/book-store/request/31108999-977842ee-cde1-4162-929c-a481be152525">This</a> endpoint is intended for user log in. Example of request body:</p>
<pre style="margin-top: -0.6pc;">
  <code>
{
  "email": "john.doe@example.com",
  "password": "securePassword123",
}
  </code>
</pre>

<p style="font-size: 20px; margin-top: 0pc;">Book Controller</p>
<p style="margin-top: -1pc;"><code>/api/books  // GET</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;"><a href="https://www.postman.com/lunar-module-cosmologist-43034160/workspace/book-store/request/31108999-36a39eaf-e60e-48d7-a61b-5710284dc8a2">This</a> endpoint is intended for users to view all available books in the store. Example of response body:</p>
<pre style="margin-top: -0.6pc;">
  <code>
[
    {
        "id": 1,
        "title": "The Great Gatsby",
        "author": "F. Scott Fitzgerald",
        "isbn": "9780743273565",
        "price": 11,
        "description": "The story of the fabulously wealthy Jay Gatsby",
        "coverImage": "https://example.com/book1-cover-image.jpg",
        "categoriesIds": [
            1
        ]
    },
    {
        "id": 2,
        "title": "Pride and Prejudice",
        "author": "Jane Austen",
        "isbn": "9780141439518",
        "price": 20,
        "description": "A romantic novel",
        "coverImage": "https://example.com/book2-cover-image.jpg",
        "categoriesIds": [
            2
        ]
    },
    {
        "id": 3,
        "title": "1984",
        "author": "George Orwell",
        "isbn": "9780451524935",
        "price": 9,
        "description": "A dystopian social science fiction novel",
        "coverImage": "https://example.com/book3-cover-image.jpg",
        "categoriesIds": [
            2
        ]
    }
]
  </code>
</pre>
<p style="margin-top: 1.5pc;"><code>/api/books/{bookId}  // GET</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;"><a href="https://www.postman.com/lunar-module-cosmologist-43034160/workspace/book-store/request/31108999-2e319ebd-0188-4671-b748-f85c10de2852">This</a> endpoint is intended for users to view a specific book in the store. Example of response body:</p>
<pre style="margin-top: -0.6pc;">
  <code>
{
        "id": 1,
        "title": "The Great Gatsby",
        "author": "F. Scott Fitzgerald",
        "isbn": "9780743273565",
        "price": 11,
        "description": "The story of the fabulously wealthy Jay Gatsby",
        "coverImage": "https://example.com/book1-cover-image.jpg",
        "categoriesIds": [
            1
        ]
}
  </code>
</pre>
<p style="margin-top: 1.5pc;"><code>/api/books/search?titles=params&authors=params  // GET</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;"><a href="https://www.postman.com/lunar-module-cosmologist-43034160/workspace/book-store/request/31108999-380994e6-d18a-43fc-b05a-2e49dcc52f17">This</a> endpoint is intended for users to filter books by titles and authors. Example of response body:</p>
<pre style="margin-top: -0.6pc;">
  <code>
{
        "id": 2,
        "title": "Pride and Prejudice",
        "author": "Jane Austen",
        "isbn": "9780141439518",
        "price": 20,
        "description": "A romantic novel",
        "coverImage": "https://example.com/book2-cover-image.jpg",
        "categoriesIds": [
            2
        ]
    }
  </code>
</pre>
<p style="margin-top: 0pc;"><code>/api/books  // POST</code></p>
<p style="margin-top: 0pc;"><code>/api/books/{bookId}  // PUT</code></p>
<p style="margin-top: 0pc;"><code>/api/books/{bookId}  // DELETE</code></p>
<p style="font-size: 16px; margin-top: -0.6pc;">These endpoints are intended for administrators to add, update, and delete books from the store.</p>

<p style="font-size: 20px; margin-top: 0pc;">Category Controller</p>
<p style="margin-top: 0pc;"><code>/api/categories  // POST</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;"><a href="">This</a> endpoint is intended for administrators to add a new category.</p>
<p style="margin-top: 0pc;"><code>/api/categories  // GET</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;"><a href="https://www.postman.com/lunar-module-cosmologist-43034160/workspace/book-store/request/31108999-ca8a3aad-6573-4714-9eec-dc49e5477de1">This</a> endpoint is intended for users to view all categories.</p>
<p style="margin-top: 0pc;"><code>/api/categories/{categoryId}  // GET</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;"><a href="https://www.postman.com/lunar-module-cosmologist-43034160/workspace/book-store/request/31108999-dc1a7cc7-5cd2-4733-89c4-7468d099c53c">This</a> endpoint is intended for users to view a specific category.</p>
<p style="margin-top: 0pc;"><code>/api/categories/{categoryId}  // PUT</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for administrators to update a specific category.</p>
<p style="margin-top: 0pc;"><code>/api/categories/{categoryId}  // DELETE</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for administrators to delete a specific category.</p>
<p style="margin-top: 0pc;"><code>/api/categories/{categoryId}/books  // GET</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;"><a href="https://www.postman.com/lunar-module-cosmologist-43034160/workspace/book-store/request/31108999-9c04d487-23ee-4400-931d-01eb6f929437">This</a> endpoint is intended for users to view all books by category id.</p>

<p style="font-size: 20px; margin-top: 0pc;">Order Controller</p>
<p style="margin-top: 0pc;"><code>/api/orders  // POST</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;"><a href="https://www.postman.com/lunar-module-cosmologist-43034160/workspace/book-store/request/31108999-41fe61d5-0b3c-4573-acde-c8dc9ffd7b87">This</a> endpoint is intended for users to place an order.</p>
<p style="margin-top: 0pc;"><code>/api/orders  // GET</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;"><a href="https://www.postman.com/lunar-module-cosmologist-43034160/workspace/book-store/request/31108999-ed3c9548-d8fd-414d-a1f1-0f4d74f9fafa">This</a> endpoint is intended for users to view all their orders.</p>
<p style="margin-top: 0pc;"><code>/api/orders/{orderId}  // PATCH</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for administrators to change status a specific order.</p>
<p style="margin-top: 0pc;"><code>/api/orders/{orderId}/items  // GET</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;"><a href="https://www.postman.com/lunar-module-cosmologist-43034160/workspace/book-store/request/31108999-8fc63728-b8f2-428b-9521-4233a9c7d4f0">This</a> endpoint is intended for users to view all order items by their order id.</p>
<p style="margin-top: 0pc;"><code>/api/orders/{orderId}/items/{itemId}  // GET</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;"><a href="https://www.postman.com/lunar-module-cosmologist-43034160/workspace/book-store/request/31108999-51737882-1539-4312-8ca9-7616a546fa2d">This</a> endpoint is intended for users to view a specific order item from their order.</p>

<p style="font-size: 20px; margin-top: 0pc;">Shopping Cart Controller</p>
<p style="margin-top: 0pc;"><code>/api/cart  // POST</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;"><a href="https://www.postman.com/lunar-module-cosmologist-43034160/workspace/book-store/request/31108999-cf26abaf-1872-4b7d-a373-cee3cd6f0eb5">This</a> endpoint is intended for users to put a cart item into their shopping cart.</p>
<p style="margin-top: 0pc;"><code>/api/cart  // GET</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;"><a href="https://www.postman.com/lunar-module-cosmologist-43034160/workspace/book-store/request/31108999-49359f4a-f2c0-42d2-bccb-281cba9a1f1d">This</a> endpoint is intended for users to view their shopping cart.</p>
<p style="margin-top: 0pc;"><code>/api/cart/cart-items/{id}  // PUT</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;"><a href="https://www.postman.com/lunar-module-cosmologist-43034160/workspace/book-store/request/31108999-62918f38-17dd-495a-84f5-31ebc7634ff5">This</a> endpoint is intended for users to update quantity of the cart item.</p>
<p style="margin-top: 0pc;"><code>/api/cart/{cartItemId}  // DELETE</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;"><a href="https://www.postman.com/lunar-module-cosmologist-43034160/workspace/book-store/request/31108999-859f0364-6b72-4b24-bdd5-64fb20ce7ed1">This</a> endpoint is intended for users to delete a specific cart item from their shopping cart.</p>

<h1 style="font-size: 24.5px; margin-top: -1pc; margin-left: 0pc;">History of creating the project</h1>
<h1 style="font-size: 16px; margin-top: -1pc; margin-left: 0pc;">Docker and test issues</h1>
<p style="font-size: 16px; margin-top: -0.8pc;">During the project development, I faced several issues. The first issue was with docker, my configurations weren't correct, so it caused my application to fail to launch. To resolve this, I discovered a sample docker-compose.yml file online and restructured mine accordingly. Additionally, I faced issues with tests. Initially, I had implemented incorrect logic in the JwtAuthenticationFilter. Because of this, my tests didn't pass. However, after understanding the problem, I rewrote the filter, solving the issues 🔧😅.</p>
<h1 style="font-size: 16px; margin-top: -1pc; margin-left: 0pc;">Liquibase and security issues</h1>
<p style="font-size: 16px; margin-top: -0.8pc;">Also, I faced problems with Liquibase as I was creating tables in the wrong sequence. Consequently, my application failed to start. Upon debugging, I identified the mistake and restructured everything correctly 😅. Moreover, I encountered difficulties with security settings. I couldn't access a public endpoint because I didn't specify in the security configuration which endpoints could be accessed by unregistered users. While searching for a solution online, I found the 'authorizeHttpRequests' method, which resolved my issue 😊🔒.</p>
<h1 style="font-size: 16px; margin-top: -1pc; margin-left: 0pc;">Possible improvements</h1>
<p style="font-size: 16px; margin-top: -0.8pc;">Furthermore, to enhance my project, I plan to add a 'Response' controller. This controller will allow users to give feedback and ratings (from 1 to 10) for each book they purchase. This functionality aims to provide users with insights into recommended books based on ratings and reviews 😃📚.</p>

<h1 style="font-size: 24.5px; margin-top: -1pc; margin-left: 0pc;">Ability to send requests to the endpoints by Postman</h1>
<p style="font-size: 16px; margin-top: -0.8pc;">Moreover, you can try sending requests to the endpoints using Postman by clicking <a href="https://www.postman.com/lunar-module-cosmologist-43034160/workspace/book-store/collection/31108999-12fddcfe-0952-4376-8687-98fe077eea02">here</a>. It's a collection of prepared requests where you can test functionality of the controllers. Here are all endpoints, to which users can send requests. Log in and registration endpoints don't need a token, but the others do. Firstly, you need to register if you haven't already. Next, you'll need to log in to obtain a token. After that, you must pass the token in the header authorization in the Bearer Token field. To elaborate on that, I recorded a video where I showed how all endpoints work, including endpoints, which are accessible for administrators. You can watch it by clicking <a href="https://www.loom.com/share/9b0fba93601a4fa3a9c5181a172bc297">here</a> 😊🎥.</p>
