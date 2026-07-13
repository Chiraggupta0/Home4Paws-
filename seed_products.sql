-- Home4Paws store seed data: 38 dog & cat products.
-- Run this on Supabase AFTER the `products` table exists (Phase A migration).
-- Images are colour-coded placeholders (by type) that always load and are copyright-safe.
-- Replace image_url per product via the admin form later if you want real photos.

INSERT INTO products (name, category, type, price, description, image_url, stock) VALUES
-- Dog · Food (orange)
('Chicken & Rice Adult Dog Food 3kg','Dog','Food',899,'Complete nutrition for adult dogs with real chicken and wholesome rice.','https://placehold.co/500x500/E05A1C/FFFFFF?text=Chicken+Rice+Dog+Food',60),
('Puppy Starter Kibble 1.5kg','Dog','Food',649,'Small-bite kibble packed with DHA for growing puppies.','https://placehold.co/500x500/E05A1C/FFFFFF?text=Puppy+Kibble',80),
('Grain-Free Salmon Dog Food 2kg','Dog','Food',1199,'Grain-free recipe rich in omega-3 for skin and coat health.','https://placehold.co/500x500/E05A1C/FFFFFF?text=Salmon+Dog+Food',40),
('Dental Care Dog Biscuits 500g','Dog','Food',299,'Crunchy biscuits that help reduce tartar and freshen breath.','https://placehold.co/500x500/E05A1C/FFFFFF?text=Dental+Biscuits',120),
-- Dog · Toy (gold)
('Rope Tug Chew Toy','Dog','Toy',249,'Durable cotton rope for tug-of-war and dental cleaning.','https://placehold.co/500x500/F4A735/FFFFFF?text=Rope+Tug+Toy',90),
('Squeaky Rubber Bone','Dog','Toy',199,'Tough rubber bone with a satisfying squeak dogs love.','https://placehold.co/500x500/F4A735/FFFFFF?text=Squeaky+Bone',110),
('Interactive Treat Puzzle','Dog','Toy',549,'Slow-feeder puzzle that keeps busy dogs mentally engaged.','https://placehold.co/500x500/F4A735/FFFFFF?text=Treat+Puzzle',45),
('Fetch Ball 2-Pack','Dog','Toy',179,'Bouncy high-visibility balls for endless games of fetch.','https://placehold.co/500x500/F4A735/FFFFFF?text=Fetch+Balls',130),
-- Dog · Accessory (brown)
('No-Pull Adjustable Harness','Dog','Accessory',649,'Padded no-pull harness with adjustable straps for a snug fit.','https://placehold.co/500x500/9B4E20/FFFFFF?text=Dog+Harness',55),
('Reflective Nylon Leash 5ft','Dog','Accessory',349,'Strong 5ft leash with reflective stitching for night walks.','https://placehold.co/500x500/9B4E20/FFFFFF?text=Dog+Leash',70),
('Padded Collar with ID Tag','Dog','Accessory',299,'Soft padded collar including a customizable metal ID tag.','https://placehold.co/500x500/9B4E20/FFFFFF?text=Dog+Collar',85),
('Travel Water Bottle','Dog','Accessory',399,'Leak-proof bottle with a built-in bowl for walks and trips.','https://placehold.co/500x500/9B4E20/FFFFFF?text=Travel+Bottle',60),
-- Dog · Grooming (dark brown)
('Oatmeal Pet Shampoo 500ml','Dog','Grooming',349,'Gentle oatmeal shampoo that soothes itchy sensitive skin.','https://placehold.co/500x500/6B3E26/FFFFFF?text=Pet+Shampoo',75),
('Slicker Deshedding Brush','Dog','Grooming',449,'Fine-wire brush that removes loose fur and reduces shedding.','https://placehold.co/500x500/6B3E26/FFFFFF?text=Deshedding+Brush',50),
('Paw Balm and Nose Butter','Dog','Grooming',259,'Natural balm that heals dry cracked paws and noses.','https://placehold.co/500x500/6B3E26/FFFFFF?text=Paw+Balm',95),
('Nail Clipper with Guard','Dog','Grooming',229,'Sharp clippers with a safety guard to avoid over-cutting.','https://placehold.co/500x500/6B3E26/FFFFFF?text=Nail+Clipper',65),
-- Dog · Health (green)
('Multivitamin Chews 60ct','Dog','Health',599,'Daily chewable vitamins supporting immunity and energy.','https://placehold.co/500x500/1A7F3C/FFFFFF?text=Dog+Multivitamin',50),
('Tick and Flea Spot-On 3 Doses','Dog','Health',499,'Fast-acting spot-on treatment protecting against ticks and fleas.','https://placehold.co/500x500/1A7F3C/FFFFFF?text=Tick+Flea+Spot-On',70),
('Joint Care Hip Supplement','Dog','Health',799,'Glucosamine chews that support joints and mobility in older dogs.','https://placehold.co/500x500/1A7F3C/FFFFFF?text=Joint+Supplement',40),
('Probiotic Digestive Powder','Dog','Health',449,'Probiotic powder that promotes healthy digestion and gut balance.','https://placehold.co/500x500/1A7F3C/FFFFFF?text=Probiotic+Powder',55),
-- Cat · Food (orange)
('Ocean Fish Adult Cat Food 1.2kg','Cat','Food',549,'Complete adult cat food with real ocean fish and taurine.','https://placehold.co/500x500/E05A1C/FFFFFF?text=Ocean+Fish+Cat+Food',65),
('Kitten Milk Replacer 300g','Cat','Food',399,'Nutrient-rich milk replacer for orphaned or weaning kittens.','https://placehold.co/500x500/E05A1C/FFFFFF?text=Kitten+Milk',80),
('Grain-Free Chicken Cat Food 1.5kg','Cat','Food',899,'High-protein grain-free chicken recipe for indoor cats.','https://placehold.co/500x500/E05A1C/FFFFFF?text=Chicken+Cat+Food',45),
('Hairball Control Dry Food 1kg','Cat','Food',499,'Fiber-enriched formula that helps reduce hairballs.','https://placehold.co/500x500/E05A1C/FFFFFF?text=Hairball+Food',70),
-- Cat · Toy (gold)
('Catnip Feather Wand','Cat','Toy',199,'Feather teaser wand infused with catnip for active play.','https://placehold.co/500x500/F4A735/FFFFFF?text=Feather+Wand',120),
('Interactive Motion Ball','Cat','Toy',349,'Self-rolling ball that keeps cats chasing and pouncing.','https://placehold.co/500x500/F4A735/FFFFFF?text=Motion+Ball',60),
('Sisal Scratching Post','Cat','Toy',899,'Sturdy sisal post that saves your furniture from claws.','https://placehold.co/500x500/F4A735/FFFFFF?text=Scratching+Post',35),
('Crinkle Play Tunnel','Cat','Toy',549,'Collapsible tunnel with crinkle sound for hide-and-seek fun.','https://placehold.co/500x500/F4A735/FFFFFF?text=Play+Tunnel',50),
-- Cat · Accessory (brown)
('Breakaway Safety Collar with Bell','Cat','Accessory',199,'Safe breakaway collar with a bell to protect wildlife.','https://placehold.co/500x500/9B4E20/FFFFFF?text=Cat+Collar',100),
('Ceramic Tilted Food Bowl','Cat','Accessory',349,'Whisker-friendly tilted bowl for comfortable feeding.','https://placehold.co/500x500/9B4E20/FFFFFF?text=Cat+Bowl',70),
('Cozy Cave Cat Bed','Cat','Accessory',999,'Soft enclosed bed that gives cats a warm sense of security.','https://placehold.co/500x500/9B4E20/FFFFFF?text=Cat+Bed',30),
('Window Perch Hammock','Cat','Accessory',749,'Suction-mounted perch that lets cats lounge and sunbathe.','https://placehold.co/500x500/9B4E20/FFFFFF?text=Window+Perch',40),
-- Cat · Grooming (dark brown)
('Gentle Cat Shampoo 250ml','Cat','Grooming',299,'Tearless gentle shampoo formulated for cats sensitive skin.','https://placehold.co/500x500/6B3E26/FFFFFF?text=Cat+Shampoo',65),
('Self-Cleaning Slicker Brush','Cat','Grooming',399,'One-click retractable brush that removes loose fur easily.','https://placehold.co/500x500/6B3E26/FFFFFF?text=Cat+Brush',55),
('Cat Nail Scissors','Cat','Grooming',199,'Precision scissors sized for delicate cat claws.','https://placehold.co/500x500/6B3E26/FFFFFF?text=Cat+Nail+Scissors',75),
-- Cat · Health (green)
('Hairball Relief Gel','Cat','Health',349,'Malt-flavored gel that helps cats pass hairballs naturally.','https://placehold.co/500x500/1A7F3C/FFFFFF?text=Hairball+Gel',60),
('Cat Multivitamin Paste','Cat','Health',449,'Tasty paste delivering essential vitamins and minerals.','https://placehold.co/500x500/1A7F3C/FFFFFF?text=Cat+Multivitamin',50),
('Dewormer Tablets 4ct','Cat','Health',299,'Broad-spectrum dewormer for common intestinal parasites.','https://placehold.co/500x500/1A7F3C/FFFFFF?text=Cat+Dewormer',70);
