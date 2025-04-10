#!/usr/bin/env node

const { execSync } = require('child_process');

// @ts-ignore
function executeShellScript(script) {
  console.log();
  console.log('--------------------------------------------------');
  console.log('Running', script);
  console.log('--------------------------------------------------');
  execSync(script, { stdio: 'inherit' });
}

executeShellScript('npm run generate:search-api');
