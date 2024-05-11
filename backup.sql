USE [master]
GO
/****** Object:  Database [kinmel]    Script Date: 5/11/2024 2:30:58 PM ******/
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
/****** Object:  Table [dbo].[category]    Script Date: 5/11/2024 2:30:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[category](
	[category_id] [int] IDENTITY(1,1) NOT NULL,
	[category_name] [varchar](50) NULL,
	[category_description] [varchar](50) NULL,
	[category_created] [datetime2](6) NULL,
PRIMARY KEY CLUSTERED 
(
	[category_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[product_images]    Script Date: 5/11/2024 2:30:58 PM ******/
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
/****** Object:  Table [dbo].[products]    Script Date: 5/11/2024 2:30:58 PM ******/
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
PRIMARY KEY CLUSTERED 
(
	[product_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[roles]    Script Date: 5/11/2024 2:30:58 PM ******/
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
/****** Object:  Table [dbo].[roles_Assigned]    Script Date: 5/11/2024 2:30:58 PM ******/
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
/****** Object:  Table [dbo].[users]    Script Date: 5/11/2024 2:30:58 PM ******/
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
ALTER TABLE [dbo].[products] ADD  DEFAULT ((1)) FOR [product_status]
GO
ALTER TABLE [dbo].[products] ADD  DEFAULT ((0)) FOR [featured]
GO
ALTER TABLE [dbo].[products] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[products] ADD  DEFAULT (getdate()) FOR [updated_at]
GO
ALTER TABLE [dbo].[product_images]  WITH CHECK ADD FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[products]  WITH CHECK ADD FOREIGN KEY([category_id])
REFERENCES [dbo].[category] ([category_id])
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
/****** Object:  StoredProcedure [dbo].[GetAllCategories]    Script Date: 5/11/2024 2:30:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[GetAllCategories]
AS
BEGIN
    SET NOCOUNT ON;

    SELECT category_id,category_name,category_description FROM category;
END
GO
/****** Object:  StoredProcedure [dbo].[InsertNewCategory]    Script Date: 5/11/2024 2:30:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[InsertNewCategory]
@CategoryName varchar(50),
@CategoryDescription varchar(250),
@CategoryReturn varchar(50) OUTPUT
as
begin
INSERT INTO category (category_name,category_description,category_created)values (@CategoryName,@CategoryDescription,GETDATE());
SET @CategoryReturn = @CategoryName
end
GO
/****** Object:  StoredProcedure [dbo].[InsertProduct]    Script Date: 5/11/2024 2:30:58 PM ******/
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
    @ProductImagePaths VARCHAR(MAX)
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRANSACTION;

    BEGIN TRY
        -- Insert the product
        INSERT INTO products (product_name, product_description, category_id, brand, price, discounted_price, stock_quantity, product_status, featured)
        VALUES (@ProductName, @ProductDescription, @CategoryId, @Brand, @Price, @DiscountedPrice, @StockQuantity, @ProductStatus, @Featured);

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
/****** Object:  StoredProcedure [dbo].[UpdateCategory]    Script Date: 5/11/2024 2:30:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[UpdateCategory]
    @CategoryId INT,
    @CategoryName VARCHAR(50),
    @CategoryDescription VARCHAR(250),
	@UpdatedCategoryName VARCHAR(50) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    IF EXISTS (SELECT 1 FROM category WHERE category_id = @CategoryId)
    BEGIN
        UPDATE category
        SET category_name = @CategoryName,
            category_description = @CategoryDescription
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
