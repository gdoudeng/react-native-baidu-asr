const { getDefaultConfig, mergeConfig } = require('@react-native/metro-config');
const exclusionList = require('metro-config/src/defaults/exclusionList');

const path = require('node:path');
const escape = require('escape-string-regexp');
const pak = require('../package.json');

const root = path.resolve(__dirname, '..');
const modules = Object.keys({ ...pak.peerDependencies });

/**
 * Metro configuration
 * https://facebook.github.io/metro/docs/configuration
 *
 * @type {import('metro-config').MetroConfig}
 */
const config = {
  watchFolders: [root],

  // We need to make sure that only one version is loaded for peerDependencies
  // So we block them at the root, and alias them to the versions in example's node_modules
  resolver: {
    /**
     * 运行此example项目时 被link进来测试的根项目会去使用自己的 peerDependencies 依赖
     * 但它默认会去自身目录下的 node_modules 中寻找， 即 root/node_modules/${peerDependencies的依赖名}
     * 这种表现是错误的 它应该去使用 example 项目自身的依赖 (因为运行的是example项目，并且是它的peerDependencies依赖)
     * 所以应该去example项目的 node_modules 中去寻找依赖， 即 root/example/node_modules/${peerDependencies的依赖名}
     *
     * 所以在此配置 修复此问题
     *  - 将此路径的依赖设为黑名单，忽略其导入: root/node_modules/${peerDependencies的依赖名}
     *  - 提供额外的Node模块和对应的导入路径: { [peerDependencies的依赖名]: 导入路径 }
     *
     *
     * 如果不修复，则会导致这么个结果： 根模块使用 root/node_modules 下的 react-native、react 等 peerDependencies 依赖项
     * example项目使用 root/example/node_modules 下的 react-native、react 等依赖项
     * 最终会加载两个版本的依赖 每个依赖都有两个独立的实例 不能互通 进而导致莫名其妙的bug (比如原生模块Java中的事件回调触发了 但无法触发RN JavaScript中添加的回调)
     */
    // 依赖黑名单
    blacklistRE: exclusionList(
      modules.map(
        m => new RegExp(`^${escape(path.join(root, 'node_modules', m))}\\/.*$`),
      ),
    ),
    // 额外的Node模块
    extraNodeModules: modules.reduce((acc, name) => {
      acc[name] = path.join(__dirname, 'node_modules', name);
      return acc;
    }, {}),
  },

  /** 代码转换规则 */
  transformer: {
    getTransformOptions: async () => ({
      transform: {
        experimentalImportSupport: false,
        inlineRequires: true,
      },
    }),
  },
};

module.exports = mergeConfig(getDefaultConfig(__dirname), config);
