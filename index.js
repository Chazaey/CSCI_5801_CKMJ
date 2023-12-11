const express = require('express');
const mysql = require('mysql2');

// Create a MySQL pool
const pool = mysql.createPool({
    host: '192.168.1.143', // The host name of the old laptop
    user: 'API_ACCOUNT', // Your database username
    password: 'ayepeeeye123', // Your database password
    database: 'haveneed' // Your database name
});

// Promisify for Node.js async/await.
const promisePool = pool.promise();

const app = express();
const PORT = 8080;

// Middleware to parse request body as JSON
app.use(express.json());

// Get items by FamilyID and ItemStatus
app.get('/items/:familyId/:itemStatus', async (req, res) => {
    try {
        const { familyId, itemStatus } = req.params;
        const [rows] = await promisePool.query('SELECT * FROM Item WHERE FamilyID = ? AND ItemStatus = ? ORDER BY Name', [familyId, itemStatus]);
        res.json(rows);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});


// Get family by Email and Name
app.get('/families/:email/:name', async (req, res) => {
    const { email, name } = req.params;
    try {
        const [rows] = await promisePool.query('SELECT * FROM Family WHERE Email = ? AND Name = ?', [email, name]);
        if (rows.length > 0) {
            res.json(rows[0]);
        } else {
            res.status(404).send('Family not found');
        }
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// Get family by FamilyID
app.get('/family/:familyId', async (req, res) => {
    try {
        const familyId = parseInt(req.params.familyId);
        console.log(familyId);
        const [rows] = await promisePool.query('SELECT familyId, email, name FROM Family WHERE FamilyID = ?', [familyId]);
        if (rows.length > 0) {
            res.json(rows[0]);
        } else {
            res.status(404).send('Family not found');
        }
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// Post a new family
app.post('/families', async (req, res) => {
    const { name, email } = req.body;
    try {
        const [result] = await promisePool.query('INSERT INTO Family (Name, Email) VALUES (?, ?)', [name, email]);
        res.status(201).json({ familyId: result.insertId, name, email });
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});



// Delete an item
app.delete('/items/:itemId', async (req, res) => {
    const { itemId } = req.params;
    try {
        await promisePool.query('DELETE FROM Item WHERE ItemID = ?', [itemId]);
        res.status(200).send('Item deleted successfully');
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// Update item status to 'Have'
app.put('/item/:itemId/have', async (req, res) => {
    try {
        const itemId = req.params.itemId;
        const [result] = await promisePool.query('UPDATE Item SET ItemStatus = "Have" WHERE ItemID = ?', [itemId]);
        res.json({ message: 'Item updated to Have', itemId: itemId });
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// Update item status to 'Need'
app.put('/item/:itemId/need', async (req, res) => {
    try {
        const itemId = req.params.itemId;
        const [result] = await promisePool.query('UPDATE Item SET ItemStatus = "Need" WHERE ItemID = ?', [itemId]);
        res.json({ message: 'Item updated to Need', itemId: itemId });
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// Search items by wildcarded name, familyID, and Itemstatus
app.get('/items/search', async (req, res) => {
    const { familyId, itemStatus, searchQuery } = req.query;
    try {
        const [rows] = await promisePool.query(
            'SELECT ItemID AS itemId, Name AS name, Amount AS amount, ItemStatus AS itemStatus, FamilyID AS familyId FROM Item WHERE FamilyID = ? AND ItemStatus = ? AND Name LIKE CONCAT("%", ?, "%") ORDER BY Name',
            [familyId, itemStatus, searchQuery]
        );
        res.json(rows);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// Post a new item
app.post('/items', async (req, res) => {
    const { name, amount, itemStatus, familyId } = req.body;
    try {
        const [result] = await promisePool.query('INSERT INTO Item (Name, Amount, ItemStatus, FamilyID) VALUES (?, ?, ?, ?)', [name, amount, itemStatus, familyId]);
        res.status(201).json({ itemId: result.insertId, name, amount, itemStatus, familyId });
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

app.listen(
    PORT,
    () => {console.log(`Server running on port ${PORT}`);}
);


