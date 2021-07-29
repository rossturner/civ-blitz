/**
 * Imports
 */
var os = require('os'),
    path = require('path'),
    fs = require('fs'),
    exec = require('child_process').exec;

/**
 * Execute a command synchronously, until node 0.12
 *
 * Uses shell.js approach to emulate child_process.execSync
 * http://strongloop.com/strongblog/whats-new-in-node-js-v0-12-execsync-a-synchronous-api-for-child-processes/
 */
function execSync(command) {
  var time = process.hrtime(),
      unique = process.pid + '-' + time[0] + '-' + time[1] + '-' + (Math.random() * 0x100000000 + 1).toString(36),
      outputFile = path.resolve(os.tmpdir(), unique + '.output'),
      completeFile = path.resolve(os.tmpdir(), unique + '.complete'),
      output;
  exec(command + ' 2>&1 1>' + outputFile + ' && echo 1 > ' + completeFile);
  while (!fs.existsSync(completeFile)) {}
  output = fs.readFileSync(outputFile);
  fs.unlinkSync(outputFile);
  fs.unlinkSync(completeFile);
  return output;
}

/**
 * Generate os.networkInferfaces with "mac" property, until node 0.12
 *
 * @returns   {Object}    list of network interfaces
 */
module.exports = function() {
  var interfaces = os.networkInterfaces(),
      platform = os.platform();
  Object.keys(interfaces).forEach(function(name) {
    var mac,
        matches;
    if (platform === 'linux' || platform === 'darwin') {
      mac = execSync('ifconfig ' + name);
    } else if (platform === 'win32' || platform === 'win64') {
      mac = execSync('getmac /fo csv /nh /v | find "' + name + '"');
    }
    if (!(mac && /^([0-9a-f]{1,2}[\.:-]){5}[0-9f-f]{1,2}$/i.test(mac))) {
      mac = '00:00:00:00:00:00';
    }
    interfaces[name].forEach(function(iface) {
      iface.mac = mac;
    });
  });
  return interfaces;
};
