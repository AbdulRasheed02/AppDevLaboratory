const mongoose = require('mongoose')
const Schema = mongoose.Schema;

const fileSchema = new Schema({
    email: String,
    uri: String
})

const fileModel = mongoose.model('fileDetail', fileSchema);
//fileDetail must be SINGULAR!!
module.exports = fileModel;