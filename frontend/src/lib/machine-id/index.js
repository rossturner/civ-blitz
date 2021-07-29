/**
 * Imports
 */
var os = require('os'),
    crypto = require('crypto'),
    path = require('path'),
    networkInterfaces = require('./network.js');

/**
 * Retrieve unique MAC addresses
 *
 * @returns   {Array}   array of MAC addresses
 */
function macAddresses() {
  var interfaces = networkInterfaces(),
      addresses = {};
  Object.keys(interfaces).forEach(function(key) {
    interfaces[key].forEach(function(value) {
      if (value.mac && !addresses[value.mac]) {
        addresses[value.mac] = true;
      }
    });
  });
  return Object.keys(addresses).sort();
}

/**
 * Generate the machine ID
 *
 * @return    {String}    machine ID
 */
function generate() {
  var id = os.cpus().map(function(cpu) { return cpu.model; }).join(':') + '|' + os.totalmem() + '|' + macAddresses();
  return crypto.createHash('md5').update(id).digest('hex');
}

/**
 * Machine ID
 */
var ID = generate(),
    FORMATTED = ID.substring(0, 8) + '-' + ID.substring(8, 12) + '-' + ID.substring(12, 16) + '-' + ID.substring(16, 20) + '-' + ID.substring(20);

/**
 * Generate the machine ID
 *
 * @param   {Bolean}    [format]    true to return a formatted ID
 * @returns {String}    machine ID
 */
module.exports = function(format) {
  return format === true ? FORMATTED : ID;
};
