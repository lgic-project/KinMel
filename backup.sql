USE [master]
GO
/****** Object:  Database [kinmel]    Script Date: 6/23/2024 9:01:55 PM ******/
CREATE DATABASE [kinmel]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'kinmel', FILENAME = N'C:\SQLData\kinmel.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'kinmel_log', FILENAME = N'C:\SQLData\kinmel_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [kinmel] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [kinmel].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [kinmel] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [kinmel] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [kinmel] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [kinmel] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [kinmel] SET ARITHABORT OFF 
GO
ALTER DATABASE [kinmel] SET AUTO_CLOSE ON 
GO
ALTER DATABASE [kinmel] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [kinmel] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [kinmel] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [kinmel] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [kinmel] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [kinmel] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [kinmel] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [kinmel] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [kinmel] SET  ENABLE_BROKER 
GO
ALTER DATABASE [kinmel] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [kinmel] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [kinmel] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [kinmel] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [kinmel] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [kinmel] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [kinmel] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [kinmel] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [kinmel] SET  MULTI_USER 
GO
ALTER DATABASE [kinmel] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [kinmel] SET DB_CHAINING OFF 
GO
ALTER DATABASE [kinmel] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [kinmel] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [kinmel] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [kinmel] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
ALTER DATABASE [kinmel] SET QUERY_STORE = ON
GO
ALTER DATABASE [kinmel] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [kinmel]
GO
/****** Object:  Table [dbo].[cart]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[cart](
	[cart_id] [int] IDENTITY(1,1) NOT NULL,
	[buyer_id] [int] NOT NULL,
	[product_id] [int] NOT NULL,
	[quantity] [int] NOT NULL,
	[added_at] [datetime] NULL,
	[total] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[cart_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[category]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[category](
	[category_id] [int] IDENTITY(1,1) NOT NULL,
	[category_name] [varchar](50) NOT NULL,
	[category_description] [varchar](250) NULL,
	[category_created] [datetime2](6) NULL,
	[category_image] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[category_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[category_request]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[category_request](
	[category_request_id] [int] IDENTITY(1,1) NOT NULL,
	[category_request_name] [varchar](50) NULL,
	[category_request_description] [varchar](250) NULL,
	[category_request_image_path] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[category_request_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[order_items]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[order_items](
	[order_item_id] [int] IDENTITY(1,1) NOT NULL,
	[order_id] [int] NOT NULL,
	[product_id] [int] NOT NULL,
	[quantity] [int] NOT NULL,
	[total_price] [bigint] NOT NULL,
	[order_Status] [varchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[order_item_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[orders]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[orders](
	[order_id] [int] IDENTITY(1,1) NOT NULL,
	[buyer_id] [int] NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[phone_number] [nvarchar](20) NOT NULL,
	[address] [nvarchar](500) NOT NULL,
	[payment_method] [nvarchar](20) NOT NULL,
	[order_total] [bigint] NOT NULL,
	[ordered_at] [datetime] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[order_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[product_images]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[product_images](
	[product_image_id] [int] IDENTITY(1,1) NOT NULL,
	[product_id] [int] NULL,
	[product_image_path] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[product_image_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[product_rating]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[product_rating](
	[ratingId] [int] IDENTITY(1,1) NOT NULL,
	[product_id] [int] NOT NULL,
	[rating] [decimal](2, 1) NOT NULL,
	[buyer_id] [int] NOT NULL,
	[created_at] [datetime2](7) NULL,
PRIMARY KEY CLUSTERED 
(
	[ratingId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[products]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[products](
	[product_id] [int] IDENTITY(1,1) NOT NULL,
	[product_name] [varchar](255) NOT NULL,
	[product_description] [varchar](300) NULL,
	[category_id] [int] NULL,
	[brand] [varchar](100) NULL,
	[price] [bigint] NOT NULL,
	[discounted_price] [bigint] NULL,
	[stock_quantity] [int] NOT NULL,
	[product_status] [int] NULL,
	[featured] [int] NULL,
	[created_at] [datetime] NULL,
	[updated_at] [datetime] NULL,
	[seller_id] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[product_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[roles]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[roles](
	[role_id] [int] IDENTITY(1,1) NOT NULL,
	[name] [varchar](50) NULL,
	[description] [varchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[role_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[roles_Assigned]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[roles_Assigned](
	[user_id] [int] NULL,
	[role_id] [int] NULL,
	[roles_assigned_id] [int] IDENTITY(1,1) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[roles_assigned_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[users]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[users](
	[user_id] [int] IDENTITY(1,1) NOT NULL,
	[first_name] [varchar](50) NULL,
	[last_name] [varchar](50) NULL,
	[email] [varchar](50) NULL,
	[password] [varchar](256) NULL,
	[address] [varchar](50) NULL,
	[phone_number] [bigint] NULL,
	[profile_photo] [varchar](300) NULL,
	[otp] [varchar](255) NULL,
	[otp_generated_time] [datetime2](6) NULL,
	[active] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[cart] ADD  DEFAULT ((1)) FOR [quantity]
GO
ALTER TABLE [dbo].[cart] ADD  DEFAULT (getdate()) FOR [added_at]
GO
ALTER TABLE [dbo].[order_items] ADD  DEFAULT ('Pending') FOR [order_Status]
GO
ALTER TABLE [dbo].[orders] ADD  DEFAULT (getdate()) FOR [ordered_at]
GO
ALTER TABLE [dbo].[product_rating] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[products] ADD  DEFAULT ((1)) FOR [product_status]
GO
ALTER TABLE [dbo].[products] ADD  DEFAULT ((0)) FOR [featured]
GO
ALTER TABLE [dbo].[products] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[products] ADD  DEFAULT (getdate()) FOR [updated_at]
GO
ALTER TABLE [dbo].[cart]  WITH CHECK ADD FOREIGN KEY([buyer_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[cart]  WITH CHECK ADD FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[order_items]  WITH CHECK ADD FOREIGN KEY([order_id])
REFERENCES [dbo].[orders] ([order_id])
GO
ALTER TABLE [dbo].[order_items]  WITH CHECK ADD FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[orders]  WITH CHECK ADD FOREIGN KEY([buyer_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[product_images]  WITH CHECK ADD FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[product_rating]  WITH CHECK ADD FOREIGN KEY([buyer_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[product_rating]  WITH CHECK ADD FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[products]  WITH CHECK ADD FOREIGN KEY([category_id])
REFERENCES [dbo].[category] ([category_id])
GO
ALTER TABLE [dbo].[products]  WITH CHECK ADD FOREIGN KEY([seller_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[roles_Assigned]  WITH CHECK ADD FOREIGN KEY([role_id])
REFERENCES [dbo].[roles] ([role_id])
GO
ALTER TABLE [dbo].[roles_Assigned]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[roles_Assigned]  WITH CHECK ADD  CONSTRAINT [FKoukh9m3eeolsw4j9yyvbtrt2m] FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[roles_Assigned] CHECK CONSTRAINT [FKoukh9m3eeolsw4j9yyvbtrt2m]
GO
ALTER TABLE [dbo].[roles_Assigned]  WITH CHECK ADD  CONSTRAINT [FKpjacbqelplbt8bub6a730w2us] FOREIGN KEY([role_id])
REFERENCES [dbo].[roles] ([role_id])
GO
ALTER TABLE [dbo].[roles_Assigned] CHECK CONSTRAINT [FKpjacbqelplbt8bub6a730w2us]
GO
ALTER TABLE [dbo].[orders]  WITH CHECK ADD CHECK  (([payment_method]='Khalti' OR [payment_method]='COD'))
GO
/****** Object:  StoredProcedure [dbo].[DeleteCartById]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[DeleteCartById]
    @cartId INT,
    @result NVARCHAR(200) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Attempt to delete the cart item
        DELETE FROM [dbo].[cart]
        WHERE [cart_id] = @cartId;

        -- Check if any rows were affected
        IF @@ROWCOUNT > 0
            SET @result = 'Cart item deleted successfully.';
        ELSE
            SET @result = 'Cart item not found.';

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        DECLARE @ErrorMessage NVARCHAR(4000) = ERROR_MESSAGE();
        DECLARE @ErrorSeverity INT = ERROR_SEVERITY();
        DECLARE @ErrorState INT = ERROR_STATE();

        -- Set the result to indicate an error occurred
        SET @result = 'Error: ' + @ErrorMessage;

        -- Re-raise the error for the client to handle
        RAISERROR(@ErrorMessage, @ErrorSeverity, @ErrorState);
    END CATCH;
END;
GO
/****** Object:  StoredProcedure [dbo].[FindProductById]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
       CREATE PROCEDURE [dbo].[FindProductById]
	   @ProductId int 
	   as
	   begin
	   SELECT
            p.product_id,
            p.product_name,
            p.product_description,
            c.category_name,
            p.brand,
            p.price,
            p.discounted_price,
            p.stock_quantity,
            p.product_status,
            p.featured,
			P.seller_id,
            p.created_at,
            p.updated_at,
			 STRING_AGG(COALESCE(pim.product_image_path, ''), ', ') AS product_images
        FROM
            products p
            INNER JOIN category c ON p.category_id = c.category_id
			 LEFT JOIN product_images pim ON p.product_id = pim.product_id
			where p.product_id= @ProductId
			  GROUP BY
        p.product_id,
        p.product_name,
        p.product_description,
        c.category_name,
        p.brand,
        p.price,
		p.discounted_price,
        p.stock_quantity,
        p.product_status,
        p.featured,
		p.seller_id,
        p.created_at,
        p.updated_at
		end
GO
/****** Object:  StoredProcedure [dbo].[GetAllCartByUserId]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
Create Procedure [dbo].[GetAllCartByUserId] 
@buyerId INT
AS
begin
select  cart.cart_id, 
  SUBSTRING(
    STRING_AGG(COALESCE(product_images.product_image_path, ''), ', '),
    1,
    CHARINDEX(',', STRING_AGG(COALESCE(product_images.product_image_path, ''), ', ')) - 1
  ) AS product_image_path
,products.product_name,products.price,products.discounted_price,cart.quantity ,cart.total
from cart inner join products on cart.product_id=products.product_id inner join product_images on 
product_images.product_id=products.product_id
where cart.buyer_id=@buyerId
group by product_name,price,discounted_price,quantity,total,cart_id

end
GO
/****** Object:  StoredProcedure [dbo].[GetAllCategories]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[GetAllCategories]
AS
BEGIN
    SET NOCOUNT ON;

    SELECT category_id,category_name,category_description,category_image FROM category;
END
GO
/****** Object:  StoredProcedure [dbo].[InsertNewCart]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[InsertNewCart]
    @buyer_id INT,
    @product_id INT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @discounted_price DECIMAL(10, 2);
    DECLARE @total DECIMAL(10, 2);
    DECLARE @existing_cart_id INT;

    -- Check if a cart item already exists for the given buyer and product
    SELECT @existing_cart_id = cart_id
    FROM cart
    WHERE buyer_id = @buyer_id
        AND product_id = @product_id;

    -- If a cart item already exists, update the quantity and total
    IF @existing_cart_id IS NOT NULL
    BEGIN
        SELECT @discounted_price = p.discounted_price
        FROM products p
        WHERE p.product_id = @product_id;

        UPDATE cart
        SET quantity = quantity + 1,
            total = (@discounted_price * (quantity + 1))
        WHERE cart_id = @existing_cart_id;
    END
    ELSE
    BEGIN
        -- If no cart item exists, insert a new record
        SELECT @discounted_price = discounted_price
        FROM products
        WHERE product_id = @product_id;

        SET @total = 1 * @discounted_price;

        INSERT INTO cart (buyer_id, product_id, quantity, added_at, total)
        VALUES (@buyer_id, @product_id, 1, GETDATE(), @total);
    END
END
GO
/****** Object:  StoredProcedure [dbo].[InsertNewCategory]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[InsertNewCategory]
@CategoryName varchar(50),
@CategoryDescription varchar(250),
@CategoryImage varchar(250),
@CategoryReturn varchar(50) OUTPUT
as
begin
INSERT INTO category (category_name,category_description,category_image,category_created)values (@CategoryName,@CategoryDescription,@CategoryImage, GETDATE());
SET @CategoryReturn = @CategoryName
end
GO
/****** Object:  StoredProcedure [dbo].[InsertProduct]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[InsertProduct]
    @ProductName VARCHAR(255),
    @ProductDescription VARCHAR(300),
    @CategoryId INT,
    @Brand VARCHAR(100),
    @Price BIGINT,
    @DiscountedPrice BIGINT,
    @StockQuantity INT,
    @ProductStatus INT = 1,
    @Featured INT = 0,
	@SellerId INT,
    @ProductImagePaths VARCHAR(MAX)
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRANSACTION;
	if @ProductStatus IS NULL begin set @ProductStatus=1 end;

    BEGIN TRY
        -- Insert the product
        INSERT INTO products (product_name, product_description, category_id, brand, price, discounted_price, stock_quantity, product_status, featured,seller_id)
        VALUES (@ProductName, @ProductDescription, @CategoryId, @Brand, @Price, @DiscountedPrice, @StockQuantity, @ProductStatus, @Featured,@SellerId);

        -- Get the newly inserted product ID
        DECLARE @ProductId INT = SCOPE_IDENTITY();

        -- Insert the product images
        INSERT INTO product_images (product_id, product_image_path)
        SELECT @ProductId, value
        FROM STRING_SPLIT(@ProductImagePaths, ',');

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END
GO
/****** Object:  StoredProcedure [dbo].[SP_SortProducts]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[SP_SortProducts]
    @ProductName VARCHAR(255) = NULL,
    @BrandName VARCHAR(100) = NULL,
    @SortBy VARCHAR(50) = 'Random',
    @CategoryName VARCHAR(100) = NULL,
    @MaxPrice BIGINT = NULL
AS
BEGIN
    SET NOCOUNT ON;

	if @SortBy IS NULL begin set @SortBy='Random' end;
    -- Initial search query
    WITH SearchResults AS (
        SELECT
            p.product_id,
            p.product_name,
            p.product_description,
            c.category_name,
            p.brand,
            p.price,
            p.discounted_price,
            p.stock_quantity,
            p.product_status,
            p.featured,
			P.seller_id,
            p.created_at,
            p.updated_at
        FROM
            products p
            INNER JOIN category c ON p.category_id = c.category_id
        WHERE
            (@ProductName IS NULL OR p.product_name LIKE '%' + @ProductName + '%')
            AND (@BrandName IS NULL OR p.brand LIKE '%' + @BrandName + '%')
    )

    -- Apply sorting and filtering on the search results
    SELECT
        sr.product_id,
        sr.product_name,
        sr.product_description,
        sr.category_name,
        sr.brand,
        sr.price,
        sr.discounted_price,
        sr.stock_quantity,
        sr.product_status,
        sr.featured,
		SR.seller_id,
        sr.created_at,
        sr.updated_at,
        STRING_AGG(COALESCE(pim.product_image_path, ''), ', ') AS product_images
    FROM
        SearchResults sr
        LEFT JOIN product_images pim ON sr.product_id = pim.product_id
    WHERE
        ((@CategoryName IS NULL) OR (sr.category_name = @CategoryName))
        AND ((@MaxPrice IS NULL) OR (sr.price <= @MaxPrice))
    GROUP BY
        sr.product_id,
        sr.product_name,
        sr.product_description,
        sr.category_name,
        sr.brand,
        sr.price,
        sr.discounted_price,
        sr.stock_quantity,
        sr.product_status,
        sr.featured,
		SR.seller_id,
        sr.created_at,
        sr.updated_at
    ORDER BY
	(CASE @SortBy
            WHEN 'Random' THEN NEWID()
            ELSE NULL END),
        (CASE @SortBy
            WHEN 'New' THEN sr.created_at END) DESC,
        (CASE @SortBy
            WHEN 'Category' THEN sr.category_name
            ELSE NULL END),
        (CASE @SortBy
            WHEN 'Price' THEN sr.price
            ELSE NULL END)
			;
END
GO
/****** Object:  StoredProcedure [dbo].[UpdateCartQuantity]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[UpdateCartQuantity]
    @cart_id INT,
    @quantity_change VARCHAR(3)
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @new_quantity INT;
    DECLARE @current_quantity INT;
    DECLARE @discounted_price DECIMAL(10, 2);
    DECLARE @new_total DECIMAL(10, 2);

    SELECT
        @current_quantity = quantity,
        @discounted_price = p.discounted_price
    FROM cart c
    JOIN products p ON c.product_id = p.product_id
    WHERE c.cart_id = @cart_id;

    SET @new_quantity = CASE
                            WHEN @quantity_change = 'add' THEN @current_quantity + 1
                            WHEN @quantity_change = 'sub' AND @current_quantity > 1 THEN @current_quantity - 1
                            ELSE CAST(@quantity_change AS INT)
                        END;

    IF @new_quantity > 0
    BEGIN
        SET @new_total = @new_quantity * @discounted_price;

        UPDATE cart
        SET quantity = @new_quantity,
            total = @new_total
        WHERE cart_id = @cart_id;
    END
    ELSE
    BEGIN
        RAISERROR('Quantity cannot be zero or negative.', 16, 1);
    END
END
GO
/****** Object:  StoredProcedure [dbo].[UpdateCategory]    Script Date: 6/23/2024 9:01:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[UpdateCategory]
    @CategoryId INT,
    @CategoryName VARCHAR(50),
    @CategoryDescription VARCHAR(250),
	@CategoryImage VARCHAR(250),
	@UpdatedCategoryName VARCHAR(50) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    IF EXISTS (SELECT 1 FROM category WHERE category_id = @CategoryId)
    BEGIN
        UPDATE category
        SET category_name = @CategoryName,
            category_description = @CategoryDescription,
			category_image=@CategoryImage
        WHERE category_id = @CategoryId;
        SET @UpdatedCategoryName = @CategoryName;
    END
    ELSE
    BEGIN
        SET @UpdatedCategoryName = NULL;
    END
	END
GO
USE [master]
GO
ALTER DATABASE [kinmel] SET  READ_WRITE 
GO
