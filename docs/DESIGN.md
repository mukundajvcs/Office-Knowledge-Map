# Design System - Office Knowledge Map

## Color Palette

### Primary Colors
- **Primary Blue**: `#4A90E2` - Used for primary actions and highlights
- **Primary Purple**: `#667EEA` - Main brand color for toolbar and primary elements
- **Accent Purple**: `#764BA2` - Secondary brand color for gradients and accents
- **Accent Pink**: `#E91E63` - Used for accent elements and warnings

### Supporting Colors
- **Success Green**: `#4CAF50` - Success states and confirmations
- **Warning Orange**: `#FF9800` - Warning states and alerts

### Text Colors
- **Text Dark**: `#2C3E50` - Primary text color for headings and important content
- **Text Secondary**: `#5A6C7D` - Secondary text for descriptions and subtitles
- **Text Light**: `#95A5A6` - Placeholder text and disabled states

### Background Colors
- **Background Light**: `#F8F9FA` - Light background for sections
- **Card Background**: `#FFFFFF` - White background for cards
- **Empty State**: `#BDC3C7` - Icons and elements in empty states

## Gradients

### Background Gradient (Body)
```scss
linear-gradient(135deg, #E8EAF6 0%, #F3E5F5 50%, #FCE4EC 100%)
```
Soft gradient from light indigo → light purple → light pink

### Toolbar Gradient
```scss
linear-gradient(135deg, #667EEA 0%, #764BA2 100%)
```
Purple gradient for the main navigation toolbar

### Heading Gradient
```scss
linear-gradient(135deg, #4A90E2 0%, #764BA2 100%)
```
Blue to purple gradient for main headings (using text gradient effect)

### Subheading Gradient
```scss
linear-gradient(135deg, #4A90E2 0%, #667EEA 100%)
```
Lighter blue gradient for section titles

### Card Header Background
```scss
linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%)
```
Subtle gradient overlay for card headers

### Form Section Background
```scss
linear-gradient(135deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%)
```
Very subtle gradient for form sections

## Typography

### Font Family
- **Primary Font**: Roboto (weights: 300, 400, 500, 600, 700)
- **Icon Font**: Material Icons

### Font Sizes & Weights
- **H1 (Page Titles)**: 36px, font-weight: 700 (with gradient)
- **H2 (Card Titles)**: 32px, font-weight: 700 (with gradient)
- **H3 (Section Titles)**: 26px, font-weight: 700 (with gradient)
- **H4 (Subsection Titles)**: 22px, font-weight: 600
- **Body Text**: 16px, font-weight: 400
- **Secondary Text**: 15px, font-weight: 400
- **Small Text**: 13px, font-weight: 400

### Letter Spacing
- Headings: -0.5px (tighter for better appearance)
- Buttons: 0.5px (wider for emphasis)
- User Role Badges: 1px (uppercase labels)

## Components

### Cards
- **Background**: `rgba(255, 255, 255, 0.95)` with `backdrop-filter: blur(10px)`
- **Border Radius**: 12px
- **Shadow**: `0 8px 24px rgba(0, 0, 0, 0.08)`
- **Hover Shadow**: `0 12px 32px rgba(0, 0, 0, 0.12)`
- **Border**: `1px solid rgba(255, 255, 255, 0.9)`
- **Hover Transform**: `translateY(-2px)` with smooth transition

### Buttons
- **Border Radius**: 8px
- **Font Weight**: 500
- **Letter Spacing**: 0.5px
- **Shadow**: `0 4px 12px rgba(0, 0, 0, 0.1)`
- **Hover Shadow**: `0 6px 16px rgba(0, 0, 0, 0.15)`
- **Hover Transform**: `translateY(-2px)`

### Form Fields
- **Background**: `rgba(255, 255, 255, 0.9)`
- **Border Radius**: 8px

### Toolbar
- **Background**: Purple gradient
- **Shadow**: `0 4px 16px rgba(102, 126, 234, 0.3)`
- **Text Color**: White
- **Button Background**: `rgba(255, 255, 255, 0.1)` with border
- **Button Hover**: `rgba(255, 255, 255, 0.2)`

### User Menu
- **Header Background**: Purple gradient
- **Role Badge**: White text on `rgba(255, 255, 255, 0.2)` background
- **Border Radius**: 12px for badge

### Login/Register Cards
- **Background**: `rgba(255, 255, 255, 0.98)` with backdrop blur
- **Shadow**: `0 12px 40px rgba(0, 0, 0, 0.2)`
- **Border Radius**: 16px
- **Border**: `1px solid rgba(255, 255, 255, 0.9)`
- **Container Background**: Purple gradient (full screen)

### Tree/Hierarchy Nodes
- **Background**: Light gradient from white to very light gray
- **Border**: `1px solid rgba(102, 126, 234, 0.2)`
- **Hover Background**: Purple gradient (10% opacity)
- **Hover Transform**: `translateX(4px)`

## Scrollbar

### Track
- **Background**: `rgba(255, 255, 255, 0.5)`
- **Border Radius**: 5px

### Thumb
- **Background**: Blue to purple gradient
- **Border Radius**: 5px
- **Hover**: Stronger purple gradient

## Effects

### Backdrop Filter
Used on cards and overlays: `blur(10px)` for modern frosted glass effect

### Transitions
- **Default**: `all 0.3s ease`
- **Hover transforms**: Smooth lift effect with shadow increase
- **Icon rotations**: `0.2s` for expand/collapse animations

### Text Gradients
Main technique for colorful headings:
```scss
background: linear-gradient(135deg, #4A90E2 0%, #764BA2 100%);
-webkit-background-clip: text;
-webkit-text-fill-color: transparent;
background-clip: text;
```

## Design Principles

1. **Not Too Dark**: Light, airy backgrounds with soft gradients
2. **Not Over-Colored**: Subtle color application with focus on blue-purple spectrum
3. **Professional**: Clean, modern design suitable for office/corporate use
4. **Consistency**: Same color palette and gradient angles throughout
5. **Accessibility**: High contrast between text and backgrounds
6. **Modern**: Glassmorphism effects with backdrop blur and semi-transparent cards
7. **Interactive**: Smooth transitions and hover effects for better UX
8. **Visual Hierarchy**: Gradient text for important headings, solid colors for body text

## Browser Support

- **Backdrop Filter**: Modern browsers (Chrome 76+, Safari 9+, Firefox 103+)
- **CSS Gradients**: All modern browsers
- **Text Gradient**: All modern browsers with -webkit- prefix
- **Custom Scrollbar**: Webkit browsers (Chrome, Safari, Edge)

## Accessibility

- All text meets WCAG 2.1 AA contrast requirements
- Color is not the only indicator of state (icons and text labels included)
- Focus states maintained for keyboard navigation
- Sufficient spacing for touch targets (minimum 48px height for buttons)
