<img height="200" src="Знімок екрана 2023-12-22 171708.png" width="500" style="display: block; margin: 0 auto; margin-top: 1pc;">
<h1 style="text-align: center; font-size: 36px; margin-top: -0.5pc;">Book store</h1>
<h1 style="font-size: 24.5px; margin-top: -2pc; margin-left: 0pc;">Introduction</h1>
<p style="font-size: 16px; margin-top: -0.8pc;">Book Store is an online platform created for user convenience in searching, browsing, and purchasing books through the Internet. It solves several issues:</p>
<ul style="font-size: 16px; padding-left: 20px; margin-top: -0.8pc; margin-left: 0pc;">
    <li>Convenient book search: Users can search for books by title and author, allowing them to quickly find the desired books.</li>
    <li>Book purchasing: Users have the option to purchase books directly through this platform, conveniently selecting desired books and putting them into a shopping cart.</li>
</ul>
<h1 style="font-size: 24.5px; margin-top: -1pc; margin-left: 0pc;">Technology stack</h1>
<p style="font-size: 16px; margin-top: -0.8pc;">While working on the project, I used the following technologies: <span style="color: black; font-weight: bold;">Spring Boot, Spring Security, Spring Data JPA, Spring MVC, Swagger, Java, Hibernate, MySQL, Spring, Maven, JUnit, Mockito, Windows, Servlets, Tomcat, JSP, XML, JSON, Git, IDE.</span></p>
<h1 style="font-size: 24.5px; margin-top: -1pc; margin-left: 0pc;">Functionality of controllers</h1>
<p style="font-size: 20px; margin-top: -1pc;">Authentication Controller</p>
<p style="margin-top: -1pc;"><code>/api/auth/registration  // POST</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for user registration. Example of request body:</p>
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
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for user log in. Example of request body:</p>
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
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for users to view all available books in the store. Example of response body:</p>
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
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for users to view a specific book in the store. Example of response body:</p>
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
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for users to filter books by titles and authors. Example of response body:</p>
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
<p style="margin-top: 0pc;"><code>/api/books  // PUT</code></p>
<p style="margin-top: 0pc;"><code>/api/books  // DELETE</code></p>
<p style="font-size: 16px; margin-top: -0.6pc;">These endpoints are intended for administrators to add, update, and delete books from the store.</p>
<p style="font-size: 20px; margin-top: 0pc;">Category Controller</p>
<p style="margin-top: 0pc;"><code>/api/categories  // POST</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for administrators to add a new category.</p>
<p style="margin-top: 0pc;"><code>/api/categories  // GET</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for users to view all categories.</p>
<p style="margin-top: 0pc;"><code>/api/categories/{categoryId}  // GET</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for users to view a specific category.</p>
<p style="margin-top: 0pc;"><code>/api/categories/{categoryId}  // PUT</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for administrators to update a specific category.</p>
<p style="margin-top: 0pc;"><code>/api/categories  // DELETE</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for administrators to delete a specific category.</p>
<p style="margin-top: 0pc;"><code>/api/categories/{categoryId}/books  // GET</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for users to view all books by category id.</p>

<p style="font-size: 20px; margin-top: 0pc;">Order Controller</p>
<p style="margin-top: 0pc;"><code>/api/orders  // POST</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for users to place an order.</p>
<p style="margin-top: 0pc;"><code>/api/orders  // GET</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for users to view all their orders.</p>
<p style="margin-top: 0pc;"><code>/api/orders/{orderId}  // PATCH</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for administrators to change status a specific order.</p>
<p style="margin-top: 0pc;"><code>/api/orders/{orderId}/items  // GET</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for users to view all order items by their order id.</p>
<p style="margin-top: 0pc;"><code>/api/orders/{orderId}/items/{itemId}  // GET</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for users to view a specific order item from their order.</p>

<p style="font-size: 20px; margin-top: 0pc;">Shopping Cart Controller</p>
<p style="margin-top: 0pc;"><code>/api/cart  // POST</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for users to put a cart item into their shopping cart.</p>
<p style="margin-top: 0pc;"><code>/api/cart  // GET</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for users to view their shopping cart.</p>
<p style="margin-top: 0pc;"><code>/api/cart/cart-items/{id}  // PUT</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for users to update quantity of the cart item.</p>
<p style="margin-top: 0pc;"><code>/api/cart/{cartItemId}  // DELETE</code></p>
<p style="font-size: 16px; margin-top: -0.8pc;">This endpoint is intended for users to delete a specific cart item from their shopping cart.</p>
<h1 style="font-size: 24.5px; margin-top: -1pc; margin-left: 0pc;">History of creating the project</h1>
<p style="font-size: 16px; margin-top: -0.8pc;">During the project development, I faced several issues. The first issue was with docker, my configurations weren't correct, so it caused my application to fail to launch. To resolve this, I discovered a sample docker-compose.yml file online and restructured mine accordingly. Additionally, I faced issues with tests. Initially, 
I had implemented incorrect logic in the jwtAuthenticationFilter. Because of this, my tests didn't pass. However, after understanding the problem, I rewrote the filter, solving the issues.</p>
<h1 style="font-size: 24.5px; margin-top: -1pc; margin-left: 0pc;">Ability to send requests to the endpoints by Postman</h1>
<p style="font-size: 16px; margin-top: -0.8pc;">Moreover, you can try sending requests to the endpoints using Postman by clicking <a href="https://www.postman.com/lunar-module-cosmologist-43034160/workspace/book-store/collection/31108999-12fddcfe-0952-4376-8687-98fe077eea02">here</a>. It's a collection of prepared requests where you can test functionality of the controllers. Here are all endpoints, to which users can send requests. Log in and registration endpoints don't need a token, but the others do. Firstly, you need to register if you haven't already. Next, you'll need to log in to obtain a token. After that, you must pass the token in the header authorization in the Bearer Token field. To elaborate on that, I recorded a video where I showed how all endpoints work, including endpoints, which are accessible for administrators. You can watch it by clicking here.</p>
