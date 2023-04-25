const mongoose = require('mongoose')
const Schema = mongoose.Schema;

const userSchema = new Schema({
    uname: String,
    pass: String,
})

const userModel = mongoose.model('userDetail', userSchema);
//userDetail must be SINGULAR!!
module.exports = userModel;